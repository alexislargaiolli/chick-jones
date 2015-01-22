package fr.alex.games.entity;

public enum Effect {
	GOLD("gold.p"), DUST("dust.p"), FIRE("fire.p");
	
	String file;
	
	Effect(String f){
		file = f;
	}

	public String getFile() {
		return file;
	}
}
