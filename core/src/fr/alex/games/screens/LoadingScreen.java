package fr.alex.games.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.esotericsoftware.spine.AnimationState;
import com.esotericsoftware.spine.AnimationStateData;
import com.esotericsoftware.spine.BoneData;
import com.esotericsoftware.spine.Skeleton;
import com.esotericsoftware.spine.SkeletonData;
import com.esotericsoftware.spine.SkeletonJson;
import com.esotericsoftware.spine.SkeletonRenderer;

import fr.alex.games.AM;
import fr.alex.games.GM;

public class LoadingScreen extends MenuScreen {

	private Label message;
	private boolean finishLoading;
	private boolean initialized;
	private float counter;
	private static final float MIN_LOADING_TIME = 0;

	private SkeletonRenderer skeletonRenderer;
	private SpriteBatch batch;
	private Skeleton skeleton;
	private AnimationStateData stateData;
	private AnimationState animState;
	private BoneData root;

	@Override
	public void render(float delta) {
		finishLoading = GM.assetManager.update();
		if (finishLoading && counter > MIN_LOADING_TIME) {
			if (!initialized) {
				((GameScreen) ScreenManager.getInstance().getScreen(Screens.GAME)).init();
				initialized = true;
			}
			message.setText("Cliquez pour commencer");
		} else {
			message.setText("Chargement...");
		}
		if (Gdx.input.justTouched() && finishLoading && initialized) {
			ScreenManager.getInstance().show(Screens.GAME);
		}

		stage.act(delta);
		stage.draw();

		animState.update(delta);
		animState.apply(skeleton);
		skeleton.updateWorldTransform();

		batch.begin();
		skeletonRenderer.draw(batch, skeleton);
		batch.end();
		counter += delta;

	}

	@Override
	public void show() {
		finishLoading = false;
		initialized = false;
		counter = 0;
		AM.loadScene();
		
		super.show();
	}	

	@Override
	public void hide() {
		
	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {
		
	}

	@Override
	protected void init() {
		background.addAction(Actions.alpha(1));
		background.addAction(Actions.alpha(0, .5f));
		message = new Label("", GM.skin);
		mainTable.add(message).padTop(200);
		GM.assetManager.load("chicken/chicken.atlas", TextureAtlas.class);
		GM.assetManager.finishLoading();
		TextureAtlas atlas = GM.assetManager.get("chicken/chicken.atlas", TextureAtlas.class);
		SkeletonJson skeletonJson = new SkeletonJson(atlas);
		SkeletonData skeletonData = skeletonJson.readSkeletonData(Gdx.files.internal("chicken/chicken.json"));
		stateData = new AnimationStateData(skeletonData);
		root = skeletonData.findBone("root");
		root.setScale(1, 1);

		skeleton = new Skeleton(skeletonData);
		stateData.setMix("idle", "run", 0.4f);
		stateData.setMix("run", "idle", 0.4f);
		animState = new AnimationState(stateData);
		animState.setAnimation(0, "run", true);
		skeleton.setPosition(Gdx.graphics.getWidth() * .5f, Gdx.graphics.getHeight() * .5f);
		skeletonRenderer = new SkeletonRenderer();
		batch = new SpriteBatch();
	}

	@Override
	protected void onBack() {

	}

}
