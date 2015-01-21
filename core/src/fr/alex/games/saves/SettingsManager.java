package fr.alex.games.saves;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class SettingsManager {
	private static SettingsManager instance;
	private static final String SETTINGS_KEY = "settings";
	private static final String LANG_KEY = "lang";
	Preferences settings;

	private SettingsManager() {
		settings = Gdx.app.getPreferences(SETTINGS_KEY);
	}

	public static SettingsManager get() {
		if (instance == null)
			instance = new SettingsManager();
		return instance;
	}

	public String getLanguage() {
		return settings.getString(LANG_KEY, "fr");
	}
}
