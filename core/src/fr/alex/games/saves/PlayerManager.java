package fr.alex.games.saves;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

import fr.alex.games.items.Item;
import fr.alex.games.items.ItemType;

public class PlayerManager {
	private static PlayerManager instance;

	public static PlayerManager get() {
		if (instance == null)
			instance = new PlayerManager();
		return instance;
	}

	private static final String PLAYER_INFO_KEY = "playerInfo";
	private static final String GOLD_KEY = "gold";
	private static final String LEVEL_KEY = "level";
	private static final String LEVEL_STARS_KEY = "level_stars";
	private static final String ITEM_KEY = "ITEM";
	private static final String EQUIPED_KEY = "EQUIPED_ITEM_";

	Preferences playerInfo;

	private PlayerManager() {
		playerInfo = Gdx.app.getPreferences(PLAYER_INFO_KEY);
		Gdx.app.log("PlayerManager", "Pref read");
	}

	private void persist() {
		playerInfo.flush();
	}

	public int getGold() {
		return playerInfo.getInteger(GOLD_KEY, 100);
	}

	public void addGold(int val) {
		playerInfo.putInteger(GOLD_KEY, getGold() + val);
		persist();
	}

	public void subGold(int val) {
		playerInfo.putInteger(GOLD_KEY, getGold() - val);
		persist();
	}

	public void setLevelFinish(int lvl, int stars) {
		playerInfo.putBoolean(LEVEL_KEY + lvl, true);
		playerInfo.putInteger(LEVEL_STARS_KEY + lvl, stars);		
		persist();
	}
	
	public int getLevelStars(int lvl){
		return playerInfo.getInteger(LEVEL_STARS_KEY + lvl, 0);
	}

	public boolean hasFinishedLevel(int lvl) {
		return playerInfo.getBoolean(LEVEL_KEY + lvl, false);
	}

	public boolean hasItem(int itemId) {
		return playerInfo.getBoolean(ITEM_KEY + itemId, false);
	}
	
	public boolean isEquiped(Item item) {
		return playerInfo.getInteger(EQUIPED_KEY + item.getType(), -1) == item.getId();
	}
	
	public int getEquipedItem(ItemType type) {
		return playerInfo.getInteger(EQUIPED_KEY + type, -1);
	}
	
	public void equipedItem(Item item){
		playerInfo.putInteger(EQUIPED_KEY + item.getType(), item.getId());
		persist();
	}

	public void addItem(int itemId) {
		playerInfo.putBoolean(ITEM_KEY + itemId, true);
		persist();
	}
}
