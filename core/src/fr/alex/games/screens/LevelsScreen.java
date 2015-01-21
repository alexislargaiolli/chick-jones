package fr.alex.games.screens;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import fr.alex.games.GM;
import fr.alex.games.Level;
import fr.alex.games.saves.PlayerManager;

public class LevelsScreen extends MenuScreen {

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

	}

	@Override
	protected void onBack() {
		ScreenManager.getInstance().show(Screens.MAIN_MENU);
	}

	@Override
	public void show() {
		mainTable.clear();
		mainTable.add("Levels", "title").center().colspan(5).pad(10).expandX();
		mainTable.row().expand();

		for(int i=0; i<Level.values().length; ++i) {
			final Level lvl = Level.values()[i];
			String btTexture = "level-locked";
			boolean unlocked = false;
			if (lvl.getIndex() == 1 || PlayerManager.get().hasFinishedLevel(lvl.getIndex()-1)) {
				unlocked = true;
			}
			if (unlocked) {
				int stars = PlayerManager.get().getLevelStars(lvl.getIndex());
				switch (stars) {
				case 0:
					btTexture = "level-0";
					break;
				case 1:
					btTexture = "level-1";
					break;
				case 2:
					btTexture = "level-2";
					break;
				case 3:
					btTexture = "level-3";
					break;
				default:
					btTexture = "level-0";
					break;
				}

			}
			TextButton bt = new TextButton(lvl.getIndex() + "", GM.skin, btTexture);
			if (unlocked) {
				bt.addListener(new ClickListener() {

					@Override
					public void clicked(InputEvent event, float x, float y) {
						GM.level = lvl;
						ScreenManager.getInstance().show(Screens.LOADING);
						super.clicked(event, x, y);
					}

				});
			}
			bt.addAction(Actions.alpha(0, 0));
			bt.addAction(Actions.delay(i * .1f, Actions.alpha(1, 0.1f)));
			mainTable.add(bt).size(80, 80);
			if (lvl.getIndex() > 1 && lvl.getIndex() % 5 == 0) {
				mainTable.row();
			}
		}
		super.show();
	}
}
