package fr.alex.games.items;

import fr.alex.games.localization.LocalManager;

public enum Slot {
	WEAPON("slot.weapon.name", "slot_weapon", 1), FOOT("slot.foot.name", "slot_foot", 2);

	private String name, icon;
	private int index;

	private Slot(String name, String icon, int index) {
		this.name = LocalManager.get().getObjectString(name);
		this.icon = icon;
		this.index = index;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}
}
