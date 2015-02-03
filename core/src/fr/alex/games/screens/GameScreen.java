package fr.alex.games.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.utils.Array;
import com.esotericsoftware.spine.SkeletonRenderer;
import com.gushikustudios.rube.RubeScene;
import com.gushikustudios.rube.loader.serializers.utils.RubeImage;

import fr.alex.games.AM;
import fr.alex.games.CameraManager;
import fr.alex.games.GM;
import fr.alex.games.GameCollisions;
import fr.alex.games.HUD;
import fr.alex.games.LightManager;
import fr.alex.games.Utils;
import fr.alex.games.box2d.entities.Component;
import fr.alex.games.box2d.entities.Entity;
import fr.alex.games.box2d.entities.components.Box2dSkeletonBasic;
import fr.alex.games.box2d.entities.components.Box2dSprite;
import fr.alex.games.box2d.entities.components.Coins;
import fr.alex.games.box2d.entities.components.DestoryedEffect;
import fr.alex.games.box2d.entities.components.Destroyable;
import fr.alex.games.box2d.entities.components.HorizontalMove;
import fr.alex.games.box2d.entities.components.Mortal;
import fr.alex.games.box2d.entities.components.NormalMap;
import fr.alex.games.box2d.entities.components.Stickable;
import fr.alex.games.entity.Chicken;
import fr.alex.games.entity.Effect;
import fr.alex.games.entity.EffectManager;
import fr.alex.games.entity.Fly;
import fr.alex.games.entity.Jump;
import fr.alex.games.entity.MoveX;
import fr.alex.games.entity.SimpleSpatial;
import fr.alex.games.entity.SpineSpatial;
import fr.alex.games.entity.UserData;
import fr.alex.games.items.ActivatedSkill;
import fr.alex.games.items.ActiveSkill;
import fr.alex.games.items.ModifType;
import fr.alex.games.items.PassiveSkill;
import fr.alex.games.saves.PlayerManager;

public class GameScreen implements Screen, InputProcessor {

	public enum State {
		STARTING, PLAYING, ENDING, WIN, LOOSE, PAUSE
	}

	/**
	 * Current game state
	 */
	private State state;

	/**
	 * Tmp vector
	 */
	private static final Vector2 tmp = new Vector2();

	/**
	 * Box 2D debug renderer
	 */
	private Box2DDebugRenderer renderer;

	/**
	 * Game batch
	 */
	private SpriteBatch batch;

	/**
	 * Spine skeleton renderer
	 */
	private SkeletonRenderer skeletonRenderer;

	/**
	 * 	
	 */
	private Chicken chicken;

	/**
	 * Activated skills still active
	 */
	private Array<ActivatedSkill> activedSkills;

	/**
	 * Reloading skills
	 */
	private Array<ActivatedSkill> reloadingSkills;

	/**
	 * Array of arrows in the world
	 */
	private Array<Entity> arrows;

	private Array<Entity> entities;

	private Array<Entity> statics;

	private Array<Entity> enemies;

	/**
	 * In game interface
	 */
	private HUD hud;

	/**
	 * Counter before game start
	 */
	private float counter;

	/**
	 * X coordinate in world space to reach to win
	 */
	private float endX;

	/**
	 * True to draw box 2d debug lines
	 */
	private boolean debug;

	private LightManager lightManager;

	public GameScreen() {
		entities = new Array<Entity>();
		enemies = new Array<Entity>();
		statics = new Array<Entity>();
		activedSkills = new Array<ActivatedSkill>();
		reloadingSkills = new Array<ActivatedSkill>();
		arrows = new Array<Entity>();
		renderer = new Box2DDebugRenderer();
		batch = new SpriteBatch();
		skeletonRenderer = new SkeletonRenderer();
		skeletonRenderer.setPremultipliedAlpha(true);
		lightManager = new LightManager(batch);
	}

	public void init() {
		entities.clear();
		enemies.clear();
		arrows.clear();
		statics.clear();
		Array<Body> tmp = GM.scene.getNamed(Body.class, "chicken");
		chicken = new Chicken(tmp.get(0));
		
		//tmp.get(0).setUserData(chicken);		

		createSpatialsFromRubeImages(GM.scene);

		tmp = GM.scene.getNamed(Body.class, "end");
		endX = tmp.get(0).getPosition().x;

		hud = new HUD(this);
		GM.scene.getWorld().setContactListener(new GameCollisions());
		
		GM.scene.getWorld().destroyBody(tmp.get(0));
		tmp = GM.scene.getNamed(Body.class, "chickenSensor");
		GM.scene.getWorld().destroyBody(tmp.get(0));
	}

	@Override
	public void show() {
		GM.world = GM.scene.getWorld();
		GM.arrowFiredCount = 0;
		GM.hitCount = 0;
		GM.gold = 0;
		GM.stars = 0;
		GM.maxArrowCount = 40;
		GM.arrowCount = GM.maxArrowCount;

		counter = 0;
		arrows.clear();
		activedSkills.clear();
		reloadingSkills.clear();

		InputMultiplexer multi = new InputMultiplexer();
		multi.addProcessor(hud.getStage());
		multi.addProcessor(this);
		Gdx.input.setInputProcessor(multi);
		Gdx.input.setCatchBackKey(true);

		state = State.STARTING;
		GM.cameraManager = new CameraManager(7f, new Vector2(chicken.getX(), chicken.getY()), new Vector2(0.15f, 0.3f));
	}

	@Override
	public void hide() {

	}

	@Override
	public void render(float delta) {
		update(delta);
		draw(delta);
	}

	private void update(float delta) {
		if (state == State.PAUSE) {

		} else {
			if (Gdx.input.isKeyPressed(Keys.Z)) {
				float angle = chicken.getBow().getAngle();
				angle += 2;
				chicken.getBow().setAngle(angle);
			}
			if (Gdx.input.isKeyPressed(Keys.S)) {
				float angle = chicken.getBow().getAngle();
				angle -= 2;
				chicken.getBow().setAngle(angle);
			}
			updateWorld(delta);
			chicken.update(state, delta);
			if (state == State.STARTING) {
				if (counter > 0) {
					counter -= delta;
					hud.setMessage(Math.round(counter) + "");
				} else if (counter <= 0) {
					state = State.PLAYING;
					hud.hideMessage();
				}
			} else if (state == State.PLAYING) {
				updateSkills(delta);

				if (isLost()) {
					state = State.ENDING;
					GM.world.clearForces();
				} else if (isWin()) {
					state = State.ENDING;
					GM.world.clearForces();
				}
			} else if (state == State.ENDING) {
				if (isLost()) {
					hud.showLoose();
					state = State.LOOSE;
				} else {
					hud.showWin();
					PlayerManager.get().setLevelFinish(GM.level.getIndex(), GM.stars);
					PlayerManager.get().addGold(GM.gold);
					state = State.WIN;
				}
			}
			
			GM.cameraManager.setTargetX(chicken.getX());
			GM.cameraManager.setTargetY(chicken.getY());
			GM.cameraManager.update(delta);
		}

		hud.update(delta);

	}

	private void updateWorld(float delta) {
		GM.scene.step();
		for (int i = 0; i < arrows.size; i++) {
			Entity arrow = arrows.get(i);
			if (GM.cameraManager.isInScreen(arrow.getPosition())) {
				arrow.update(delta);
				if (arrow.isToRemove()) {
					arrows.removeValue(arrow, false);
				}
			} else {
				arrows.removeValue(arrow, false);
			}
		}
		for (int i = 0; i < entities.size; i++) {
			Entity entity = entities.get(i);
			if (GM.cameraManager.isInScreen(entity.getPosition())) {
				entity.update(delta);
				if (entity.isToRemove()) {
					entities.removeValue(entity, false);
				}
			}
		}
		for (int i = 0; i < enemies.size; i++) {
			Entity enemy = enemies.get(i);
			if (GM.cameraManager.isInScreen(enemy.getPosition())) {
				enemy.update(delta);
				if (enemy.isToRemove()) {
					enemies.removeValue(enemy, false);
				}
			}
		}
	}

	private void updateSkills(float delta) {
		for (int i = 0; i < activedSkills.size; ++i) {
			ActivatedSkill as = activedSkills.get(i);
			as.update(delta);
			if (as.isOver()) {
				reloadingSkills.add(as);
				activedSkills.removeIndex(i);
				activeSkill(false, as.getSkill());

			}
		}
		for (int i = 0; i < reloadingSkills.size; ++i) {
			ActivatedSkill as = reloadingSkills.get(i);
			as.update(delta);
			hud.updateReloadingSkill(as.getSkill(), as.getCounterValue());
			if (as.isReloaded()) {
				reloadingSkills.removeIndex(i);
				hud.skillReloaded(as.getSkill());
			}
		}
	}

	private void draw(float delta) {
		batch.setProjectionMatrix(GM.cameraManager.combined());
		batch.begin();

		lightManager.begin(delta);
		Vector2 pos = GM.cameraManager.toScreen(chicken.getX(), chicken.getY());
		lightManager.setLightPosition(pos.x, pos.y);
		
		for (int i = 0; i < statics.size; i++) {
			Entity entity = statics.get(i);
			if (GM.cameraManager.isInScreen(entity.getPosition())) {
				entity.draw(batch, skeletonRenderer, delta);
			}
		}

		for (int i = 0; i < arrows.size; i++) {
			Entity arrow = arrows.get(i);
			if (GM.cameraManager.isInScreen(arrow.getPosition())) {
				arrow.draw(batch, skeletonRenderer, delta);
			}
		}

		for (int i = 0; i < entities.size; i++) {
			Entity entity = entities.get(i);
			if (GM.cameraManager.isInScreen(entity.getPosition())) {
				entity.draw(batch, skeletonRenderer, delta);
			}
		}
		
		batch.end();

		batch.begin();

		for (int i = 0; i < enemies.size; i++) {
			Entity ennemy = enemies.get(i);
			if (GM.cameraManager.isInScreen(ennemy.getPosition())) {
				ennemy.draw(batch, skeletonRenderer, delta);
			}
		}

		chicken.draw(batch, skeletonRenderer, delta);
		batch.end();

		EffectManager.get().draw(GM.cameraManager.combined(Utils.WORLD_TO_BOX, Utils.WORLD_TO_BOX), delta);
		hud.draw();

		if (debug) {
			renderer.render(GM.scene.getWorld(), GM.cameraManager.combined());
		}
	}

	@Override
	public void resize(int width, int height) {
		lightManager.resize(width, height);
	}

	private boolean isLost() {
		return chicken.isDead();
	}

	private boolean isWin() {
		return chicken.getX() > endX;
	}

	private void createSpatialsFromRubeImages(RubeScene scene) {
		Array<RubeImage> images = scene.getImages();
		
		TextureAtlas atlas = AM.getSceneAtlas();
		Texture diffuse = AM.getSceneDiffuse();
		Texture normal = AM.getSceneNormal();

		for (int i = 0; i < images.size; i++) {
			RubeImage image = images.get(i);
			tmp.set(image.width, image.height);
			String regionName = image.file.replace(".png", "");
			TextureRegion region = atlas.findRegion(regionName);

			Entity e = new Entity();
			Boolean spine = (Boolean) GM.scene.getCustom(image, "spine");
			if (spine == null) {
				Component c = new NormalMap(e, diffuse, normal);
				e.add(c);
				c = new Box2dSprite(e, region, image.flip, image.body, image.color, tmp, image.center, image.angleInRads * MathUtils.radiansToDegrees);
				e.add(c);
				if (image.body == null) {
					statics.add(e);
				} else {
					entities.add(e);
				}
			} else {
				String file = (String) GM.scene.getCustom(image, "spineFile");
				// Component skeleton = new Box2dSkeleton(e, file, image.body,
				// tmp);
				Component skeleton = new Box2dSkeletonBasic(e, file, image.body, tmp, image.center);			

				Component normalMap = new NormalMap(e, AM.getSceneDiffuse(), AM.getSpineNormal());
				e.add(normalMap);
				e.add(skeleton);
				enemies.add(e);
			}
			Boolean active = (Boolean) GM.scene.getCustom(image, "active");
			if (active != null && active) {
				Boolean destroyable = (Boolean) GM.scene.getCustom(image, "destroyable");
				if (destroyable != null && destroyable) {
					e.add(new Destroyable(e));
					e.add(new DestoryedEffect(e, Effect.GOLD));
				}

				Boolean stick = (Boolean) GM.scene.getCustom(image, "stick");
				if (stick != null && stick) {
					e.add(new Stickable(e, -1));
				}

				Float speedX = (Float) GM.scene.getCustom(image, "speedX");
				if (speedX != null) {
					e.add(new HorizontalMove(e, 3, -1f));
				}

				Boolean mortal = (Boolean) GM.scene.getCustom(image, "mortal");
				if (mortal != null && mortal) {
					e.add(new Mortal(e));
				}
				Boolean coin = (Boolean) GM.scene.getCustom(image, "coin");
				if (coin != null && coin) {
					Integer coins = (Integer) GM.scene.getCustom(image, "coins");
					int val = 1;
					if (coins != null) {
						val = (Integer) coins;
					}
					e.add(new Coins(e, val));
					e.add(new DestoryedEffect(e, Effect.GOLD));
				}
			}
		}

		if (false) {// (images != null) && (images.size > 0)) {

			for (int i = 0; i < images.size; i++) {
				RubeImage image = images.get(i);
				tmp.set(image.width, image.height);
				String textureFileName = image.file.replace(".png", "");
				TextureRegion region = atlas.findRegion(textureFileName);

				if (region == null) {
					//region = GM.commonAtlas.findRegion(textureFileName);
				}
				SimpleSpatial spatial = null;
				Boolean spine = (Boolean) GM.scene.getCustom(image, "spine");

				if (spine != null) {
					String file = (String) GM.scene.getCustom(image, "spineFile");
					spatial = new SpineSpatial(file, image.flip, image.body, image.color, tmp, image.center, image.angleInRads);
				} else {
					spatial = new SimpleSpatial(region, diffuse, normal, image.flip, image.body, image.color, tmp, image.center, image.angleInRads * MathUtils.radiansToDegrees);
				}
				Boolean active = (Boolean) GM.scene.getCustom(image, "active");
				if (active != null && active) {
					UserData userData = new UserData(spatial);
					spatial.setUserData(userData);

					Boolean fly = (Boolean) GM.scene.getCustom(image, "fly");
					if (fly != null && fly) {
						Fly p = new Fly(userData);
						spatial.addProperties(p);
					}

					Integer life = (Integer) GM.scene.getCustom(image, "life");
					if (life != null) {
						userData.setLife(life);
					}

					Float speedX = (Float) GM.scene.getCustom(image, "speedX");
					if (speedX != null) {
						MoveX m = new MoveX(userData, spatial.getmBody().getPosition().cpy(), 1f, speedX);
						spatial.addProperties(m);
					}

					Float durationBeforeRemove = (Float) GM.scene.getCustom(image, "durationBeforeDie");
					if (durationBeforeRemove != null) {
						userData.setTimeBeforeDie(durationBeforeRemove);
					}

					Float jumpSpeed = (Float) GM.scene.getCustom(image, "jumpSpeed");
					if (jumpSpeed != null) {
						Float jumpFrequence = (Float) GM.scene.getCustom(image, "jumpFrequence");
						if (jumpFrequence == null) {
							jumpFrequence = 5f;
						}
						Jump j = new Jump(userData, jumpSpeed, jumpFrequence);
						spatial.addProperties(j);
					}

					Integer imgCount = (Integer) GM.scene.getCustom(image, "imageCount");
					if (imgCount != null) {
						spatial.setImages(new TextureRegion[imgCount - 1]);
						spatial.setImageCount(imgCount);
						for (int j = 0; j < imgCount - 1; ++j) {
							String key = "image" + (j + 1);
							String img = (String) GM.scene.getCustom(image, key);
							spatial.getImages()[j] = atlas.findRegion(img);
						}
					}
					spatial.setUserData(userData);
				}
			}
		}
	}	

	public void activeSkill(boolean active, ActiveSkill skill) {
		if (active) {
			activedSkills.add(new ActivatedSkill(skill));
		}
		for (PassiveSkill ps : skill.getPassives()) {
			handlePassiveSkill(active, ps);
		}
	}

	private void handlePassiveSkill(boolean enable, PassiveSkill skill) {
		switch (skill.getCarac()) {
		case BOW_STRENGTH:
			chicken.getBow().setStrength(handleBonus(enable, chicken.getBow().getStrength(), skill.getType(), skill.getBonus()));
			break;
		case TIME_SPEED:
			GM.scene.setStepsPerSecond((int) handleBonus(enable, GM.scene.getStepsPerSecond(), skill.getType(), skill.getBonus()));
			chicken.setTimeFactor(handleBonus(enable, chicken.getTimeFactor(), ModifType.DIV, skill.getBonus()));
			break;
		case SPEED:
			break;
		default:
			break;
		}
	}

	private float handleBonus(boolean enable, float val, ModifType type, float bonus) {
		float res = val;
		switch (type) {
		case ADD:
			res = enable ? val + bonus : val - bonus;
			break;
		case DIV:
			res = enable ? val / bonus : val * bonus;
			break;
		case MUL:
			res = enable ? val * bonus : val / bonus;
			break;
		case SUB:
			res = enable ? val - bonus : val + bonus;
			break;
		}
		return res;
	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void dispose() {
		GM.scene.getWorld().dispose();
		GM.scene.clear();
		renderer.dispose();
		batch.dispose();
	}

	@Override
	public boolean keyDown(int keycode) {
		if (keycode == Keys.W) {
			chicken.toggleStop();
		}
		if (keycode == Keys.Q) {
			chicken.jump();
		}
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		if (keycode == Keys.BACK || keycode == Keys.SPACE) {
			if (state == State.PLAYING) {
				state = State.PAUSE;
				hud.showPause();
			} else if (state == State.PAUSE) {
				hud.hidePause();
				state = State.PLAYING;
			}
		}
		if (keycode == Keys.D) {
			debug = !debug;
		}
		
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		if (state == State.PLAYING) {
			if (Gdx.input.getX() > GM.cameraManager.getScreenCenterX()) {
				Vector2 touch = GM.cameraManager.toWorld(screenX, screenY);

				tmp.set(chicken.getBow().getOrigin());
				tmp.sub(touch.x, touch.y);
				chicken.getBow().setAngle(tmp.angle() + 180);
				arrows.addAll(chicken.getBow().fire());
				GM.arrowFiredCount++;
			}
			else if(Gdx.input.getX() <= GM.cameraManager.getScreenCenterX()){
				chicken.jump();
			}
		}
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {

		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {

		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}

	public Chicken getPlayer() {
		return chicken;
	}

	public void setPlayer(Chicken player) {
		this.chicken = player;
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}
}
