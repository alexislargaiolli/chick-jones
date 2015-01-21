package fr.alex.games.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import fr.alex.games.GM;

public class MainMenuScreen extends MenuScreen {

	public static boolean playAnim = true;

	public MainMenuScreen() {
		super();
	}

	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void show() {
		super.show();
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

	Image botarrow;
	Image toparrow;
	Image chick;
	Image jones;
	Image and;
	ImageButton btShop;
	ImageButton btEquip;
	ImageButton btPlay;

	@Override
	protected void init() {
		TextureAtlas logo = GM.assetManager.get("ui/logo/logo.atlas");
		and = new Image(logo.findRegion("&"));
		float logoX = Gdx.graphics.getWidth() * .5f - and.getWidth() * .5f;
		float logoY = Gdx.graphics.getHeight() * .8f;

		// bottom arrow
		botarrow = new Image(logo.findRegion("botarrow"));
		botarrow.setX(logoX - botarrow.getWidth() * .8f);
		botarrow.setY(logoY - botarrow.getHeight() * .5f);
		botarrow.setOrigin(botarrow.getWidth() * .5f, botarrow.getHeight() * .5f);
		stage.addActor(botarrow);

		// &
		and.setOrigin(and.getWidth() * .5f, and.getHeight() * .5f);
		and.setX(logoX);
		and.setY(logoY);
		stage.addActor(and);

		// top arrow
		toparrow = new Image(logo.findRegion("toparrow"));
		toparrow.setX(logoX + toparrow.getWidth() * 0.15f);
		toparrow.setY(logoY + toparrow.getHeight() * 0.4f);
		toparrow.setOrigin(toparrow.getWidth() * .5f, toparrow.getHeight() * .5f);
		stage.addActor(toparrow);

		// chick
		chick = new Image(logo.findRegion("chick"));
		chick.setOrigin(chick.getWidth() * .5f, chick.getHeight() * .5f);
		chick.setX(logoX - chick.getWidth() * .85f);
		chick.setY(logoY);
		stage.addActor(chick);

		// jones
		jones = new Image(logo.findRegion("jones"));
		jones.setOrigin(jones.getWidth() * .5f, jones.getHeight() * .5f);
		jones.setX(logoX + jones.getWidth() * .3f);
		jones.setY(logoY - jones.getHeight() * .15f);
		stage.addActor(jones);

		// mainTable.add("Chicken Jones", "title").expandX();
		mainTable.row().expand();
		// mainTable.row();

		btShop = new ImageButton(GM.skin.getDrawable("shop"));
		btShop.addListener(new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {
				ScreenManager.getInstance().show(Screens.SHOP);
				super.clicked(event, x, y);
			}

		});
		btShop.pad(10, 0, 10, 0);
		mainTable.add(btShop);
		// mainTable.row().expand();

		btPlay = new ImageButton(GM.skin.getDrawable("play"));
		btPlay.addListener(new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {
				ScreenManager.getInstance().show(Screens.LEVEL_MENU);

				super.clicked(event, x, y);
			}

		});
		btPlay.pad(10, 0, 10, 0);
		mainTable.add(btPlay);

		btEquip = new ImageButton(GM.skin.getDrawable("equip"));
		btEquip.addListener(new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {
				ScreenManager.getInstance().show(Screens.PLAYER);
				super.clicked(event, x, y);
			}

		});
		btEquip.pad(10, 0, 10, 0);
		mainTable.add(btEquip);
		// mainTable.row();
		animLogo();
	}

	private void animLogo() {
		float delay = 0;
		float breathAnimDuration = 1f;
		float breathScaleTo = 1.1f;
		if (playAnim) {
			playAnim = false;			
			// and
			and.addAction(Actions.moveBy(0, -Gdx.graphics.getHeight() * .3f));
			and.addAction(Actions.scaleTo(2f, 2f));
			and.addAction(Actions.alpha(0));
			and.addAction(Actions.alpha(1, 0.1f));
			and.addAction(Actions.scaleTo(1f, 1f, 0.1f));

			delay += 0.5f;
			chick.addAction(Actions.moveBy(0, -Gdx.graphics.getHeight() * .3f));
			chick.addAction(Actions.scaleTo(2f, 2f));
			chick.addAction(Actions.alpha(0));
			chick.addAction(Actions.delay(delay, Actions.alpha(1, 0.1f)));
			chick.addAction(Actions.delay(delay, Actions.scaleTo(1f, 1f, 0.1f)));

			delay += 0.5f;
			jones.addAction(Actions.moveBy(0, -Gdx.graphics.getHeight() * .3f));
			jones.addAction(Actions.scaleTo(2f, 2f));
			jones.addAction(Actions.alpha(0));
			jones.addAction(Actions.delay(delay, Actions.alpha(1, 0.1f)));
			jones.addAction(Actions.delay(delay, Actions.scaleTo(1f, 1f, 0.1f)));

			delay += 0.5f;
			// bottom arrow
			botarrow.addAction(Actions.moveBy(0, -Gdx.graphics.getHeight() * .3f));
			botarrow.addAction(Actions.alpha(0f));
			botarrow.addAction(Actions.delay(delay, Actions.alpha(1f, 0.1f)));
			botarrow.addAction(Actions.delay(delay, Actions.moveBy(-600, -200, 0f)));
			botarrow.addAction(Actions.delay(delay, Actions.moveBy(600, 200, 0.1f)));

			// top arrow
			toparrow.addAction(Actions.moveBy(0, -Gdx.graphics.getHeight() * .3f));
			toparrow.addAction(Actions.alpha(0f));
			toparrow.addAction(Actions.delay(delay, Actions.alpha(1f, 0.1f)));
			toparrow.addAction(Actions.delay(delay, Actions.moveBy(-600, -200, 0f)));
			toparrow.addAction(Actions.delay(delay, Actions.moveBy(600, 200, 0.1f)));

			delay += 0.5f;
			
			toparrow.addAction(Actions.delay(delay, Actions.moveBy(0, Gdx.graphics.getHeight() * .3f, 1f, Interpolation.swing)));
			botarrow.addAction(Actions.delay(delay, Actions.moveBy(0, Gdx.graphics.getHeight() * .3f, 1f, Interpolation.swing)));
			chick.addAction(Actions.delay(delay, Actions.moveBy(0, Gdx.graphics.getHeight() * .3f, 1f, Interpolation.swing)));
			jones.addAction(Actions.delay(delay, Actions.moveBy(0, Gdx.graphics.getHeight() * .3f, 1f, Interpolation.swing)));
			and.addAction(Actions.delay(delay, Actions.moveBy(0, Gdx.graphics.getHeight() * .3f, 1f, Interpolation.swing)));
			delay += 0.5f;
						
			botarrow.addAction(Actions.delay(delay, Actions.repeat(100, Actions.sequence(Actions.scaleTo(breathScaleTo, breathScaleTo, breathAnimDuration), Actions.scaleTo(1f, 1f, breathAnimDuration)))));
			toparrow.addAction(Actions.delay(delay, Actions.repeat(100, Actions.sequence(Actions.scaleTo(breathScaleTo, breathScaleTo, breathAnimDuration), Actions.scaleTo(1f, 1f, breathAnimDuration)))));
			chick.addAction(Actions.delay(delay, Actions.repeat(100, Actions.sequence(Actions.scaleTo(breathScaleTo, breathScaleTo, breathAnimDuration), Actions.scaleTo(1f, 1f, breathAnimDuration)))));
			jones.addAction(Actions.delay(delay, Actions.repeat(100, Actions.sequence(Actions.scaleTo(breathScaleTo, breathScaleTo, breathAnimDuration), Actions.scaleTo(1f, 1f, breathAnimDuration)))));
			and.addAction(Actions.delay(delay, Actions.repeat(100, Actions.sequence(Actions.scaleTo(breathScaleTo, breathScaleTo, breathAnimDuration), Actions.scaleTo(1f, 1f, breathAnimDuration)))));			
		}
		btPlay.addAction(Actions.alpha(0));
		btShop.addAction(Actions.alpha(0));
		btEquip.addAction(Actions.alpha(0));

		btPlay.addAction(Actions.delay(delay, Actions.alpha(1, 0.5f)));
		delay += 0.5f;
		btShop.addAction(Actions.delay(delay, Actions.alpha(1, 0.5f)));
		btEquip.addAction(Actions.delay(delay, Actions.alpha(1, 0.5f)));
		delay += 0.5f;
		//btPlay.addAction(Actions.delay(delay, Actions.repeat(100, Actions.sequence(Actions.scaleTo(breathScaleTo, breathScaleTo, breathAnimDuration), Actions.scaleTo(1f, 1f, breathAnimDuration)))));
		//btShop.addAction(Actions.delay(delay, Actions.repeat(100, Actions.sequence(Actions.scaleTo(breathScaleTo, breathScaleTo, breathAnimDuration), Actions.scaleTo(1f, 1f, breathAnimDuration)))));
		//btEquip.addAction(Actions.delay(delay, Actions.repeat(100, Actions.sequence(Actions.scaleTo(breathScaleTo, breathScaleTo, breathAnimDuration), Actions.scaleTo(1f, 1f, breathAnimDuration)))));
	}

	@Override
	protected void onBack() {
		animLogo();
	}

}
