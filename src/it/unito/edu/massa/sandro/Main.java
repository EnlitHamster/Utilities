package it.unito.edu.massa.sandro;

import java.util.HashMap;
import java.util.Set;

import it.unito.edu.massa.sandro.gui.MainFrame;

public class Main {

	private static HashMap<String, String> Entries = new HashMap<String, String>();

	public static void main(String[] args) {
		System.getProperties().forEach((k, v) -> {
			Entries.put(k.toString(), v.toString());
			System.out.println(k);
		});
		
		new MainFrame();
	}

	public static Set<String> keys() {
		return Entries.keySet();
	}

	public static String property(String key) {
		return Entries.get(key);
	}

}
