package it.unito.edu.massa.sandro.info;

import java.util.ArrayList;
import java.util.Iterator;

public class PropCategory {

	private final String family;
	private ArrayList<String> keys = new ArrayList<String>();
	private ArrayList<PropCategory> sons = new ArrayList<PropCategory>();

	public PropCategory(String familyPath) {
		this.family = familyPath;
	}

	public boolean add(PropCategory prop) {
		return sons.add(prop);
	}

	public boolean add(String key) {
		return keys.add(key);
	}

	public Iterator<PropCategory> sons() {
		return sons.iterator();
	}

	public Iterator<String> keys() {
		return keys.iterator();
	}

	public String getPath() {
		return family;
	}

	public boolean join(PropCategory prop) {
		if (family.equalsIgnoreCase(prop.family)) {
			keys.addAll(prop.keys);

			for (PropCategory son : prop.sons) {
				if (sons.contains(son))
					sons.get(sons.indexOf(son)).join(son);
				else
					sons.add(son);
			}

			return true;
		}
		return false;
	}

	public String toString() {
		return family;
	}
	
	public boolean equals(Object obj) {
		if (obj != null && obj instanceof PropCategory) {
			PropCategory objProp = (PropCategory) obj;
			if (family.equalsIgnoreCase(objProp.family))
				return true;
		}
		
		return false;
	}

}
