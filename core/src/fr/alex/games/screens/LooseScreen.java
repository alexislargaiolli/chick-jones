package fr.alex.games.screens;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import fr.alex.games.GM;
import fr.alex.games.localization.LocalManager;

public class LooseScreen extends MenuScreen {

	
	
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
	protected void init() {
		mainTable.add(LocalManager.get().getHUDString("loose.title"), "title").expandX().row();
		TextButton btReplay = new TextButton("Recommencer", GM.skin);
		btReplay.addListener(new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {				
				ScreenManager.getInstance().show(fr.alex.games.screens.Screens.LEVEL_MENU);				
				super.clicked(event, x, y);
			}

		});	
		btReplay.pad(0, 10, 10, 10);
		mainTable.add(btReplay).expand();
	}

	@Override
	protected void onBack() {
		ScreenManager.getInstance().show(Screens.MAIN_MENU);
	}

	@Override
	public void show() {
		
		super.show();
	}

}