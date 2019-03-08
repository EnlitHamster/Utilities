package it.unito.edu.massa.sandro.adapters;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;

import it.unito.edu.massa.sandro.Main;
import it.unito.edu.massa.sandro.gui.MainFrame;
import it.unito.edu.massa.sandro.info.PropCategory;

public class TreeAdapter implements TreeSelectionListener {

	private MainFrame frame;

	public TreeAdapter(MainFrame frame) {
		this.frame = frame;
	}

	@Override
	public void valueChanged(TreeSelectionEvent event) {
		JTree tree = (JTree) event.getSource();
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();

		if (node != null && node.getUserObject() instanceof PropCategory) {
			PropCategory prop = (PropCategory) node.getUserObject();

			String path = generatePath((DefaultMutableTreeNode) tree.getModel().getRoot(), node);
			frame.setPropText();
			
			generateValues(path, prop).forEach(line -> {
				frame.addPropText(line);
			});
			
			frame.endPropText();
		}
	}

	public static String generatePath(DefaultMutableTreeNode root, DefaultMutableTreeNode node) {
		DefaultMutableTreeNode next = node;
		String path = ((PropCategory) next.getUserObject()).getPath();
		while (!(next = (DefaultMutableTreeNode) next.getParent()).equals(root))
			path = ((PropCategory) next.getUserObject()).getPath().concat("." + path);

		return path;
	}

	public static List<String> generateValues(String path, PropCategory prop) {
		List<String> values = new ArrayList<String>();
		
		prop.keys().forEachRemaining(k -> {
			String key = path + '.' + k;
			String value = Main.property(key);
			values.add(key + ": " + value);
		});
		
		prop.sons().forEachRemaining(p -> {
			values.addAll(generateValues(path.concat('.' + p.getPath()), p));
		});
		
		return values;
	}

}
