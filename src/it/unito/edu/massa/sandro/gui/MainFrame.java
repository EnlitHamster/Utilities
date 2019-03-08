package it.unito.edu.massa.sandro.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.Collection;
import java.util.HashMap;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import it.unito.edu.massa.sandro.Main;
import it.unito.edu.massa.sandro.adapters.TreeAdapter;
import it.unito.edu.massa.sandro.info.PropCategory;

public class MainFrame extends JFrame {

	private static final long serialVersionUID = -5166715694727076597L;

	private JTabbedPane tabs = new JTabbedPane(JTabbedPane.TOP);
	private JSplitPane pTab = new JSplitPane();
	private JScrollPane pScrollValues = new JScrollPane();
	private JScrollPane pScrollList = new JScrollPane();
	private JLabel pValues = new JLabel();
	private JTree pList = new JTree();

	public MainFrame() {

		GridBagLayout layout = new GridBagLayout();
		layout.columnWidths = new int[] { 0, 0 };
		layout.rowHeights = new int[] { 0, 0 };
		layout.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		layout.rowWeights = new double[] { 1.0, Double.MIN_VALUE };

		// Frame setup

		setTitle("Utilities :: Sandro Massa :: 0.1");
		setResizable(true);
		setSize(800, 400);
		setLocationRelativeTo(null);
		getContentPane().setLayout(layout);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		// System Properties

		Collection<PropCategory> props = loadProps().values();
		DefaultTreeModel model = (DefaultTreeModel) pList.getModel();
		DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
		root = new DefaultMutableTreeNode("System properties");
		model.setRoot(root);

		int n = 0;
		for (PropCategory prop : props)
			model.insertNodeInto(generateNode(prop), root, n);

		pValues.setVerticalAlignment(JLabel.TOP);

		// Listeners

		TreeAdapter tsl = new TreeAdapter(this);

		pList.addTreeSelectionListener(tsl);

		// Generating Frame

		pScrollList.setViewportView(pList);
		pScrollValues.setViewportView(pValues);

		pTab.setLeftComponent(pScrollList);
		pTab.setRightComponent(pScrollValues);

		tabs.addTab("Properties", null, pTab, null);

		getContentPane().add(tabs, buildConstraints(GridBagConstraints.BOTH, 0, 0));

		// Finalizing

		setVisible(true);

	}

	public void setPropText() {
		pValues.setText("<html><body style=\"margin:10 10 10 10;text-alignment:left;\"><pre>");
	}

	public void addPropText(String text) {
		String old = pValues.getText();
		pValues.setText(old + escape(text) + "\n");
	}

	public void endPropText() {
		String old = pValues.getText();
		old = old.substring(0, old.lastIndexOf('\n'));
		pValues.setText(old + "</pre></body></html>");
	}

	private GridBagConstraints buildConstraints(int fill, int x, int y) {
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = fill;
		gbc.gridx = x;
		gbc.gridy = y;

		return gbc;
	}

	private static HashMap<String, PropCategory> loadProps() {
		HashMap<String, PropCategory> props = new HashMap<String, PropCategory>();

		Main.keys().forEach(key -> {
			int index = key.indexOf('.');
			String family = key.substring(0, index);
			PropCategory prop = new PropCategory(family);
			nextProp(prop, key.substring(index + 1));
			if (props.containsKey(family))
				props.get(family).join(prop);
			else
				props.put(family, prop);
		});

		return props;
	}

	private static void nextProp(PropCategory parent, String path) {
		if (path.contains(".")) {
			int index = path.indexOf('.');
			String family = path.substring(0, index);
			PropCategory son = new PropCategory(family);
			nextProp(son, path.substring(index + 1));
			if (parent == null)
				parent = son;
			else
				parent.add(son);
		} else {
			parent.add(path);
		}
	}

	private static DefaultMutableTreeNode generateNode(PropCategory prop) {
		DefaultMutableTreeNode node = new DefaultMutableTreeNode(prop);

		prop.sons().forEachRemaining(son -> {
			node.add(generateNode(son));
		});

		return node;
	}

	private static String escape(String str) {
		String escaped = "";
		char[] exps = { '\t', '\b', '\n', '\r', '\f', '\\' };

		boolean cnts = false;

		for (char chr : exps)
			cnts = cnts || str .indexOf(chr) >= 0;

		if (cnts)
			for (char chr : str.toCharArray()) {
				switch (chr) {
				case '\t':
					escaped = escaped.concat("\\t");
					break;
				case '\b':
					escaped = escaped.concat("\\b");
					break;
				case '\n':
					escaped = escaped.concat("\\n");
					break;
				case '\r':
					escaped = escaped.concat("\\r");
					break;
				case '\f':
					escaped = escaped.concat("\\f");
					break;
				case '\\':
					escaped = escaped.concat("\\\\");
					break;
				default:
					escaped = escaped + chr;
				}
			}
		else
			escaped = str;

		return escaped;
	}

}
