package fr.alex.games.screens;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.utils.Array;
import com.esotericsoftware.spine.SkeletonRenderer;
import com.gushikustudios.rube.RubeScene;
import com.gushikustudios.rube.loader.serializers.utils.RubeImage;

import fr.alex.games.GM;
import fr.alex.games.GameCollisions;
import fr.alex.games.HUD;
import fr.alex.games.LightManager;
import fr.alex.games.Main;
import fr.alex.games.Utils;
import fr.alex.games.box2d.entities.Component;
import fr.alex.games.box2d.entities.Entity;
import fr.alex.games.box2d.entities.HorizontalMove;
import fr.alex.games.box2d.entities.NormalMapComponent;
import fr.alex.games.box2d.entities.SkeletonComponent;
import fr.alex.games.box2d.entities.SpriteComponent;
import fr.alex.games.box2d.entities.Stickable;
import fr.alex.games.entity.Chicken;
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
	 * Camera
	 */
	private OrthographicCamera camera;

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

	/**
	 * Map of util texture regions
	 */
	private Map<String, TextureRegion> mRegionMap;

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
	 * Current camera viewport width
	 */
	public static float viewportWidth;

	/**
	 * Current camera viewport height
	 */
	public static float viewportHeight;

	/**
	 * Delta x for camera position relative to player position
	 */
	private float cameraDeltaX = 0f;

	/**
	 * Delta y for camera position relative to player position
	 */
	private float cameraDeltaY = 1.5f;	

	/**
	 * Last screen touch position used to compute bow direction
	 */
	private Vector3 lastTouch;

	/**
	 * True to draw box 2d debug lines
	 */
	private boolean debug;

	private LightManager lightManager;
	
	public static final short CHICKEN_ENTITY = 0x0001;    // 0001
	public static final short ARROW_ENTITY = 0x0002;
	public static final short WORLD_ENTITY = 0x0004;
    
	public static final short MASK_CHICKEN = WORLD_ENTITY;
	public static final short MASK_ARROW = WORLD_ENTITY;
	public static final short MASK_WORLD = -1;

	public GameScreen() {
		entities = new Array<Entity>();
		activedSkills = new Array<ActivatedSkill>();
		reloadingSkills = new Array<ActivatedSkill>();
		arrows = new Array<Entity>();
		mRegionMap = new HashMap<String, TextureRegion>();
		renderer = new Box2DDebugRenderer();
		batch = new SpriteBatch();
		skeletonRenderer = new SkeletonRenderer();
		skeletonRenderer.setPremultipliedAlpha(true);
		lastTouch = new Vector3();
		lightManager = new LightManager(batch);

	}

	public void init() {
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		viewportHeight = 7f; // 6 meters in box 2d world
		viewportWidth = viewportHeight * w / h;
		camera = new OrthographicCamera(viewportWidth, viewportHeight);
		cameraDeltaX = viewportWidth * .25f;
		GM.ratio = (float) Gdx.graphics.getHeight() / viewportHeight;
		entities.clear();
		arrows.clear();
		Array<Body> tmp = GM.scene.getNamed(Body.class, "chicken");
		TextureAtlas common = GM.assetManager.get(Main.COMMON_ATLAS_PATH, TextureAtlas.class);
		Texture commonDiffuse = GM.assetManager.get(Main.COMMON_ATLAS_PATH.replace(".atlas", "-diffuse.png"), Texture.class);
		Texture commonNormal = GM.assetManager.get(Main.COMMON_ATLAS_PATH.replace(".atlas", "-normal.png"), Texture.class);
		mRegionMap.put("arrow", GM.commonAtlas.findRegion("arrow"));
		chicken = new Chicken(tmp.get(0), mRegionMap.get("arrow"), commonDiffuse, commonNormal);
		tmp = GM.scene.getNamed(Body.class, "chickenSensor");
		tmp.get(0).setUserData(chicken);

		createSpatialsFromRubeImages(GM.scene);

		tmp = GM.scene.getNamed(Body.class, "end");
		endX = tmp.get(0).getPosition().x;

		hud = new HUD(this);
		GM.scene.getWorld().setContactListener(new GameCollisions());
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
		lastTouch.set(1, 0, 0);

		InputMultiplexer multi = new InputMultiplexer();
		multi.addProcessor(hud.getStage());
		multi.addProcessor(this);
		Gdx.input.setInputProcessor(multi);
		Gdx.input.setCatchBackKey(true);

		state = State.STARTING;
		updateCamera();
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
					chicken.run();
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
				chicken.idle();
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

			updateCamera();
		}

		hud.update(delta);

	}

	private void updateWorld(float delta) {
		GM.scene.step();
		
		for (int i = 0; i < entities.size; i++) {
			Entity e = entities.get(i);			
			if (e.isToRemove()) {
				e.destroy();
				entities.removeValue(e, false);
			}
			else{
				e.update(delta);
			}
		}
		for (int i = 0; i < arrows.size; i++) {
			Entity a = arrows.get(i);			
			if (a.isToRemove()) {
				a.destroy();
				arrows.removeValue(a, false);
			}
			else{				
				a.update(delta);
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
		batch.setProjectionMatrix(camera.combined);
		batch.begin();

		lightManager.begin();
		lightManager.getLightPosition().x = Gdx.input.getX();
		lightManager.getLightPosition().y = (Gdx.graphics.getHeight() - Gdx.input.getY());

		for (int i = 0; i < arrows.size; i++) {
			Entity a = arrows.get(i);			
			a.draw(batch, skeletonRenderer, delta);
		}

		batch.end();

		batch.begin();

		for (int i = 0; i < entities.size; i++) {
			Entity e = entities.get(i);
			if (e.contains(SpriteComponent.name)) {
				e.draw(batch, skeletonRenderer, delta);
			}
		}
		batch.end();

		batch.begin();
		for (int i = 0; i < entities.size; i++) {
			Entity e = entities.get(i);
			if (e.contains(SkeletonComponent.name)) {
				e.draw(batch, skeletonRenderer, delta);
			}
		}

		batch.end();

		batch.begin();
		chicken.draw(batch, skeletonRenderer);
		batch.end();
		
		EffectManager.get().draw(camera.combined.cpy().scale(Utils.WORLD_TO_BOX, Utils.WORLD_TO_BOX, 0), delta);
		hud.draw();

		if (debug) {
			renderer.render(GM.scene.getWorld(), camera.combined);
		}
	}

	@Override
	public void resize(int width, int height) {
		lightManager.resize(width, height);
	}

	private void updateCamera() {
		camera.position.x = chicken.getX() + cameraDeltaX;
		camera.position.y = chicken.getY() + cameraDeltaY;
		camera.update();
	}

	private boolean isLost() {
		return chicken.isDead();
	}

	private boolean isWin() {
		return chicken.getX() > endX;
	}

	private void createSpatialsFromRubeImages(RubeScene scene) {
		Array<RubeImage> images = scene.getImages();

		TextureAtlas atlas = GM.assetManager.get(Main.SCENES_ATLAS_PATH + GM.level.getSceneAtlasFile(), TextureAtlas.class);
		Texture diffuse = GM.assetManager.get(Main.SCENES_ATLAS_PATH + GM.level.getSceneAtlasFile().replace(".atlas", "-diffuse.png"), Texture.class);
		Texture normal = GM.assetManager.get(Main.SCENES_ATLAS_PATH + GM.level.getSceneAtlasFile().replace(".atlas", "-normal.png"), Texture.class);

		for (int i = 0; i < images.size; i++) {
			RubeImage image = images.get(i);
			tmp.set(image.width, image.height);
			String textureFileName = image.file.replace(".png", "");
			TextureRegion region = atlas.findRegion(textureFileName);
			if (region == null) {
				region = GM.commonAtlas.findRegion(textureFileName);
			}
			
			Entity e = new Entity();
			Boolean spine = (Boolean) GM.scene.getCustom(image, "spine");
			if (spine == null) {
				Component c = new NormalMapComponent(e, diffuse, normal);
				e.add(c);
				c = new SpriteComponent(e, region, image.flip, image.body, image.color, tmp, image.center, image.angleInRads* MathUtils.radiansToDegrees);
				e.add(c);
			} else {
				String file = (String) GM.scene.getCustom(image, "spineFile");
				GM.assetManager.load(file + "-diffuse.png", Texture.class);
				GM.assetManager.load(file + "-normal.png", Texture.class);
				Component skeleton = new SkeletonComponent(e, file, image.body.getPosition().cpy(), tmp);
				Texture spineDiffuse = GM.assetManager.get(file + "-diffuse.png", Texture.class);
				Texture spineNormal = GM.assetManager.get(file + "-normal.png", Texture.class);

				Component normalMap = new NormalMapComponent(e, spineDiffuse, spineNormal);
				e.add(normalMap);
				e.add(skeleton);
					
				GM.scene.getWorld().destroyBody(image.body);
			}
			Boolean active = (Boolean) GM.scene.getCustom(image, "active");
			if (active != null && active) {
				Boolean destroyable = (Boolean) GM.scene.getCustom(image, "destroyable");
				if (destroyable != null && destroyable) {
					//e.add(new Destroyable(e, Effect.GOLD));
				}
				
				Boolean stick = (Boolean) GM.scene.getCustom(image, "stick");
				if (stick != null && stick) {
					e.add(new Stickable(e));
				}
				
				Float speedX = (Float) GM.scene.getCustom(image, "speedX");
				if (speedX != null) {
					e.add(new HorizontalMove(e, 3, -0.01f));
				}
			}
			entities.add(e);
		}

		if (false) {// (images != null) && (images.size > 0)) {

			for (int i = 0; i < images.size; i++) {
				RubeImage image = images.get(i);
				tmp.set(image.width, image.height);
				String textureFileName = image.file.replace(".png", "");
				TextureRegion region = atlas.findRegion(textureFileName);

				if (region == null) {
					region = GM.commonAtlas.findRegion(textureFileName);
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

					Boolean destroyable = (Boolean) GM.scene.getCustom(image, "destroyable");
					if (destroyable != null && destroyable) {
						userData.setDestroyable(destroyable);
					}

					Boolean coin = (Boolean) GM.scene.getCustom(image, "coin");
					if (coin != null && coin) {
						userData.setCoin(coin);
					}

					Boolean mortal = (Boolean) GM.scene.getCustom(image, "mortal");
					if (mortal != null && mortal) {
						userData.setMortal(mortal);
					}

					Boolean stick = (Boolean) GM.scene.getCustom(image, "stick");
					if (stick != null && stick) {
						userData.setStick(stick);
					}

					Boolean star = (Boolean) GM.scene.getCustom(image, "star");
					if (star != null && star) {
						userData.setStar(star);
					}

					Boolean fly = (Boolean) GM.scene.getCustom(image, "fly");
					if (fly != null && fly) {
						Fly p = new Fly(userData);
						spatial.addProperties(p);
					}

					Integer coins = (Integer) GM.scene.getCustom(image, "coins");
					if (coins != null) {
						userData.setCoins(coins);
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

	public boolean isInScreen(float x, float y) {
		return x > (camera.position.x - camera.viewportWidth * .5f) && x < (camera.position.x + camera.viewportWidth * .5f);
	}

	public boolean isInScreen(Vector2 position) {
		return isInScreen(position.x, position.y);
	}

	public boolean hasToBeUpdated(SimpleSpatial ss) {
		if (ss.getmBody() == null) {
			return false;
		}
		return ss.getmBody().getPosition().x > (camera.position.x - camera.viewportWidth * 1.25f) && ss.getmBody().getPosition().x < (camera.position.x + camera.viewportWidth * 1.25f);
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
		if (keycode == Keys.Q) {
			chicken.jump();
			return true;
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
		if (keycode == Keys.W) {
			chicken.toggleStop();
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
			if (screenX < cameraDeltaX * GM.ratio) {
				chicken.jump();
			} else {
				lastTouch.set(screenX, screenY, 0);
				lastTouch = camera.unproject(lastTouch);

				tmp.set(chicken.getBow().getOrigin());
				tmp.sub(lastTouch.x, lastTouch.y);
				chicken.getBow().setAngle(tmp.angle() + 180);
				arrows.addAll(chicken.getBow().fire());
				GM.arrowFiredCount++;
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

	public Vector3 getLastTouch() {
		return lastTouch;
	}

	public void setLastTouch(Vector3 lastTouch) {
		this.lastTouch = lastTouch;
	}
}
