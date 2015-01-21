package fr.alex.games.screens;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import fr.alex.games.GM;

public class IntroScreen extends MenuScreen {	
	
	public IntroScreen() {
		super();
	}

	@Override
	public void render(float delta) {
		if (GM.assetManager.update()) {
			GM.itemsAtlas = GM.assetManager.get("items/items.atlas");
			ScreenManager.getInstance().show(fr.alex.games.screens.Screens.MAIN_MENU);
		}
		super.render(delta);
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void show() {
		super.show();
		GM.assetManager.load("items/items.atlas", TextureAtlas.class);
		GM.assetManager.load("ui/logo/logo.atlas", TextureAtlas.class);
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		super.dispose();
	}

	@Override
	protected void init() {
		mainTable.add("Chicken Jones", "title");
	}

	@Override
	protected void onBack() {
		
	}

}
