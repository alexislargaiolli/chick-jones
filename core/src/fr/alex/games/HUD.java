package fr.alex.games;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.alpha;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

import fr.alex.games.items.ActiveSkill;
import fr.alex.games.items.Item;
import fr.alex.games.items.ItemManager;
import fr.alex.games.localization.LocalManager;
import fr.alex.games.saves.PlayerManager;
import fr.alex.games.screens.GameScreen;
import fr.alex.games.screens.GameScreen.State;
import fr.alex.games.screens.ScreenManager;
import fr.alex.games.screens.Screens;

public class HUD {
	protected static final int VIRTUAL_WIDTH = 1280, VIRTUAL_HEIGHT = 720;

	protected Stage stage;
	protected Table mainTable;
	protected Image mask;

	// In game elements
	private Label lbInfo;
	private Label scoreLabel;
	private Label arrowLabel;
	private TextButton btResume;
	private TextButton btRestart;
	private TextButton btQuit;
	private Table tablePause;
	public static String SCORE_MESSAGE = "GOLD: ";
	public static String WIN_MESSAGE = "You win !";
	public static String LOOSE_MESSAGE = "You loose...";
	public static String CONTINUE_MESSAGE = "Click to continue";
	private GameScreen game;	

	// Game win elements
	private Label earnedGoldLabel, totalGoldLabel, arrowFiredLabel, hitCountLabel, precisionLabel;
	private TextButton btReplay, btNext;
	private float animTime1, animTime2, startGold, endGold;
	private static final float ANIM_DURATION = 1.5f;

	public HUD(final GameScreen game) {
		this.game = game;
		stage = new Stage(new ExtendViewport(VIRTUAL_WIDTH, VIRTUAL_HEIGHT));		
		mask = new Image(GM.skin.getDrawable("black"));
		mask.setZIndex(50);
		mask.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		mask.addAction(Actions.sequence(Actions.alpha(0, 0)));

		mainTable = new Table(GM.skin);
		mainTable.setFillParent(true);

		scoreLabel = new Label("" + GM.gold, GM.skin, "gold");
		scoreLabel.setAlignment(0);
		
		arrowLabel = new Label("" + GM.arrowCount, GM.skin);
		
		mainTable.add(scoreLabel).width(150).pad(10).left();
		mainTable.add(arrowLabel).pad(10).left();
		mainTable.row().expand();

		lbInfo = new Label("", GM.skin);
		mainTable.add(lbInfo).expand();

		mainTable.row();

		Table btGroup = new Table();

		for (Item item : ItemManager.get().getEquiped()) {
			for (final ActiveSkill skill : item.getActives()) {
				SpriteDrawable bg = new SpriteDrawable(GM.itemsAtlas.createSprite(item.getId() + ""));
				TextButtonStyle style = new TextButtonStyle(bg, bg, bg, GM.skin.getFont("default-font"));
				final TextButton bt = new TextButton("", style);

				bt.setName(getUiSkillKey(skill));

				bt.addListener(new ClickListener() {
					@Override
					public void clicked(InputEvent event, float x, float y) {
						if (!bt.isDisabled()) {
							game.activeSkill(true, skill);
							bt.getColor().set(Color.GRAY);
							bt.addAction(Actions.repeat(5, Actions.sequence(Actions.alpha(.5f, .5f), Actions.alpha(1f, .5f))));
							bt.setDisabled(true);
						}
						super.clicked(event, x, y);
					}
				});
				btGroup.add(bt).size(70, 70).bottom().padRight(5);
			}
		}
		
		mainTable.debugTable();
		mainTable.add(btGroup).right().bottom().pad(30);
		stage.addActor(mask);
		stage.addActor(mainTable);

		btResume = new TextButton("Reprendre", GM.skin);
		btResume.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.setState(State.PLAYING);
				hidePause();
				super.clicked(event, x, y);
			}
		});
		btRestart = new TextButton("Recommencer", GM.skin);
		btRestart.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				ScreenManager.getInstance().show(Screens.LOADING);
				hidePause();
				super.clicked(event, x, y);
			}
		});
		btQuit = new TextButton("Menu", GM.skin);
		btQuit.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				ScreenManager.getInstance().show(Screens.MAIN_MENU);
				hidePause();
				super.clicked(event, x, y);
			}
		});
		tablePause = new Table(GM.skin);
		tablePause.setFillParent(true);
		tablePause.add(btResume).row().pad(10);
		tablePause.add(btRestart).row().pad(10);
		tablePause.add(btQuit).pad(10);
		showMask(0);
		hideMask(.5f);
	}

	public void showWin() {
		mainTable.clear();
		mainTable.setZIndex(100);
		showMask(1f);

		mainTable.add(LocalManager.get().getHUDString("win.title"), "title").expandX().colspan(2).row();
		earnedGoldLabel = new Label("", GM.skin);
		mainTable.add(earnedGoldLabel).colspan(2);
		mainTable.row();

		totalGoldLabel = new Label("", GM.skin);
		mainTable.add(totalGoldLabel).colspan(2);
		mainTable.row().expand();

		arrowFiredLabel = new Label("", GM.skin);
		mainTable.add(arrowFiredLabel).colspan(2);
		mainTable.row().expand();

		hitCountLabel = new Label("", GM.skin);
		mainTable.add(hitCountLabel).colspan(2);
		mainTable.row().expand();

		precisionLabel = new Label("", GM.skin);
		mainTable.add(precisionLabel).colspan(2);
		mainTable.row().expand();

		btReplay = new TextButton(LocalManager.get().getHUDString("win.retry"), GM.skin);
		btReplay.addListener(new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {
				ScreenManager.getInstance().show(Screens.LOADING);
				super.clicked(event, x, y);
			}

		});

		btReplay.pad(0, 10, 10, 10);
		mainTable.add(btReplay).width(180);
		btNext = new TextButton(LocalManager.get().getHUDString("win.next"), GM.skin);
		btNext.addListener(new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {
				ScreenManager.getInstance().show(fr.alex.games.screens.Screens.LEVEL_MENU);
				super.clicked(event, x, y);
			}

		});
		btNext.pad(0, 10, 10, 10);
		mainTable.add(btNext).width(180);

		startGold = PlayerManager.get().getGold();
		totalGoldLabel.setText(LocalManager.get().getHudBundle().format("win.gold.total", startGold));
		endGold = startGold + GM.gold;

		earnedGoldLabel.setText("0");
		arrowFiredLabel.setText(LocalManager.get().getHudBundle().format("win.arrow.count", GM.arrowFiredCount));
		hitCountLabel.setText(LocalManager.get().getHudBundle().format("win.hit.count", GM.hitCount));
		int accuracy = 0;
		if(GM.arrowFiredCount != 0){
			accuracy = (GM.hitCount * 100 / GM.arrowFiredCount);
		}
		precisionLabel.setText(LocalManager.get().getHudBundle().format("win.accuarcy", accuracy));
		animTime1 = 0;
		animTime2 = 0;

		arrowFiredLabel.addAction(Actions.sequence(Actions.alpha(0, 0), Actions.delay(3, Actions.alpha(1f, .5f))));
		hitCountLabel.addAction(Actions.sequence(Actions.alpha(0, 0), Actions.delay(3.5f, Actions.alpha(1f, .5f))));
		precisionLabel.addAction(Actions.sequence(Actions.alpha(0, 0), Actions.delay(4f, Actions.alpha(1f, .5f))));
		btReplay.addAction(Actions.sequence(Actions.alpha(0, 0), Actions.delay(4.5f, Actions.alpha(1f, .5f))));
		btNext.addAction(Actions.sequence(Actions.alpha(0, 0), Actions.delay(5f, Actions.alpha(1f, .5f))));
	}

	public void showLoose() {
		mainTable.clear();
		mainTable.setZIndex(100);
		showMask(1f);

		Label title = new Label(LocalManager.get().getHUDString("loose.title"), GM.skin.get("title", LabelStyle.class));
		title.addAction(Actions.sequence(Actions.alpha(0, 0), Actions.delay(1, Actions.alpha(1f, 1f))));
		mainTable.add(title).expandX().row();
		TextButton btReplay = new TextButton("Recommencer", GM.skin);
		btReplay.addListener(new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {
				ScreenManager.getInstance().show(fr.alex.games.screens.Screens.LOADING);
				super.clicked(event, x, y);
			}

		});
		btReplay.addAction(Actions.sequence(Actions.alpha(0, 0), Actions.delay(1, Actions.alpha(1f, 1f))));
		btReplay.pad(0, 10, 10, 10);
		mainTable.add(btReplay).expand();
	}

	public void showPause() {
		showMask(.5f);
		stage.getActors().removeValue(mainTable, true);
		stage.addActor(tablePause);
	}

	public void hidePause() {
		hideMask(.5f);
		stage.getActors().removeValue(tablePause, true);
		stage.addActor(mainTable);
	}

	private void showMask(float duration) {
		mask.addAction(Actions.sequence(Actions.alpha(.5f, duration)));
	}

	private void hideMask(float duration) {
		mask.addAction(Actions.sequence(Actions.alpha(0, duration)));
	}

	public void updateReloadingSkill(ActiveSkill skill, int time) {
		TextButton bt = mainTable.findActor(getUiSkillKey(skill));
		bt.setText(time + "");
	}

	public void skillReloaded(ActiveSkill skill) {
		TextButton bt = mainTable.findActor(getUiSkillKey(skill));
		bt.setDisabled(false);
		bt.getColor().set(Color.WHITE);
		bt.setText("");
	}

	public String getUiSkillKey(ActiveSkill skill) {
		return skill.hashCode() + "";
	}

	public void update(float delta) {
		scoreLabel.setText(" " + GM.gold);
		arrowLabel.setText("" + GM.arrowCount);
		stage.act(delta);

		if (game.getState() == State.WIN) {
			if (animTime1 <= ANIM_DURATION) {
				int score = (int) Math.floor(Interpolation.pow2.apply(0, GM.gold, animTime1 / ANIM_DURATION));
				earnedGoldLabel.setText(LocalManager.get().getHudBundle().format("win.gold.earned", score));
				animTime1 += delta;
				if (animTime1 >= ANIM_DURATION) {
					earnedGoldLabel.setText(LocalManager.get().getHudBundle().format("win.gold.earned", GM.gold));
				}
			}
			if (animTime1 >= ANIM_DURATION && animTime2 <= ANIM_DURATION) {
				int score = (int) Math.floor(Interpolation.pow2.apply(startGold, endGold, animTime2 / ANIM_DURATION));
				totalGoldLabel.setText(LocalManager.get().getHudBundle().format("win.gold.total", score));
				animTime2 += delta;
				if (animTime2 >= ANIM_DURATION) {
					totalGoldLabel.setText(LocalManager.get().getHudBundle().format("win.gold.total", endGold));
				}
			}
		}
	}

	public void draw() {
		stage.draw();
	}

	public void setMessage(String message) {
		lbInfo.setText(message);
	}

	public void showMessage() {
		lbInfo.addAction(alpha(1, 0));

	}

	public void hideMessage() {
		lbInfo.addAction(alpha(0, 1));
	}

	public Stage getStage() {
		return stage;
	}

}
