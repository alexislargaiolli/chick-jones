package fr.alex.games.localization;

import java.util.Locale;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.I18NBundle;

import fr.alex.games.saves.SettingsManager;

public class LocalManager {

	private static LocalManager instance;

	private static final String BUNDLE_FOLDER = "i18n";
	private static final String GAME_BUNDLE = "game";
	private static final String HUD_BUNDLE = "hud";
	private static final String OBJECT_BUNDLE = "object";

	private I18NBundle gameBundle, hudBundle, objectBundle;

	private LocalManager() {
		Gdx.app.log("LocalManager", "Init");
		Locale locale = new Locale(SettingsManager.get().getLanguage());

		Gdx.app.log("LocalManager", "Read game bundle");
		FileHandle file = Gdx.files.internal(BUNDLE_FOLDER + "/" + GAME_BUNDLE);
		gameBundle = I18NBundle.createBundle(file, locale);
		Gdx.app.log("LocalManager", "Game bundle read");

		Gdx.app.log("LocalManager", "Read hud bundle");
		file = Gdx.files.internal(BUNDLE_FOLDER + "/" + HUD_BUNDLE);
		hudBundle = I18NBundle.createBundle(file, locale);
		Gdx.app.log("LocalManager", "Hud bundle read");

		Gdx.app.log("LocalManager", "Read object bundle");
		file = Gdx.files.internal(BUNDLE_FOLDER + "/" + OBJECT_BUNDLE);
		objectBundle = I18NBundle.createBundle(file, locale, "UTF-8");
		Gdx.app.log("LocalManager", "Object bundle read");
	}

	public static LocalManager get() {
		if (instance == null)
			instance = new LocalManager();
		return instance;
	}

	public String getGameString(String key) {
		return gameBundle.get(key);
	}

	public String getHUDString(String key) {
		return hudBundle.get(key);
	}

	public String getObjectString(String key) {
		return objectBundle.get(key);
	}

	public I18NBundle getGameBundle() {
		return gameBundle;
	}

	public void setGameBundle(I18NBundle gameBundle) {
		this.gameBundle = gameBundle;
	}

	public I18NBundle getHudBundle() {
		return hudBundle;
	}

	public void setHudBundle(I18NBundle hudBundle) {
		this.hudBundle = hudBundle;
	}

	public I18NBundle getObjectBundle() {
		return objectBundle;
	}

	public void setObjectBundle(I18NBundle objectBundle) {
		this.objectBundle = objectBundle;
	}

}
