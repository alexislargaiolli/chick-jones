package fr.alex.games.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.esotericsoftware.spine.SkeletonRenderer;

import fr.alex.games.GM;
import fr.alex.games.box2d.entities.Entity;
import fr.alex.games.box2d.entities.components.Box2dSkeletonBasic;
import fr.alex.games.box2d.entities.components.Collector;
import fr.alex.games.box2d.entities.components.Dieable;
import fr.alex.games.box2d.entities.components.NormalMap;
import fr.alex.games.items.Item;
import fr.alex.games.items.ItemManager;
import fr.alex.games.items.ModifType;
import fr.alex.games.items.PassiveSkill;
import fr.alex.games.screens.GameScreen.State;

public class Chicken {
	protected Vector2 jumpVector;
	private Body chicken;
	private boolean jumping;
	private float speed = 4;
	private float timeFactor = 1;
	private Bow bow;

	private float width = 1, height = 1.2f;

	private Entity chickenEntity;

	public Chicken(Body chicken, TextureRegion defaultArrowTexture, Texture diffuseArrow, Texture normalArrow) {
		this.chicken = chicken;

		chickenEntity = new Entity();
		TextureAtlas atlasDiffuse = GM.assetManager.get("chicken/chicken-diffuse.atlas", TextureAtlas.class);
		Texture atlasTexture = atlasDiffuse.getRegions().first().getTexture();
		Texture normalMapTexture = GM.assetManager.get("chicken/chicken-normal.png", Texture.class);
		chickenEntity.add(new NormalMap(chickenEntity, atlasTexture, normalMapTexture));
		Box2dSkeletonBasic skeleton = new Box2dSkeletonBasic(chickenEntity, "chicken/chicken", chicken, new Vector2(width, height), new Vector2(0, height * .5f));
		skeleton.addMix("idle", "run", 0.4f);
		skeleton.addMix("run", "jump", 0.1f);
		skeleton.addMix("jump", "run", 0.1f);
		chickenEntity.add(skeleton);
		chickenEntity.add(new Dieable(chickenEntity));
		chickenEntity.add(new Collector(chickenEntity));

		jumpVector = new Vector2(0, 300);

		idle();

		bow = new Bow(this, defaultArrowTexture, diffuseArrow, normalArrow, new Vector2(1, 1f));

		for (Item item : ItemManager.get().getEquiped()) {
			for (PassiveSkill skill : item.getPassives()) {
				handlePassiveSkill(skill);
			}
		}
	}	

	public void update(State state, float delta) {
		handleInput();
		chickenEntity.update(delta);
		bow.setOrigin(chickenEntity.getPosition().x + width * 0.3f, chickenEntity.getPosition().y + height * .2f);
		bow.update(state, delta * timeFactor);

		if (state == State.PLAYING && !isDead() && chicken.getLinearVelocity().x < speed) {
			chicken.setLinearVelocity(speed, chicken.getLinearVelocity().y);
		}
	}

	public void draw(SpriteBatch batch, SkeletonRenderer skeletonRenderer, float delta) {
		chickenEntity.draw(batch, skeletonRenderer, delta);
		bow.draw(batch, skeletonRenderer);
	}
	
	private void handleInput(){
		if(Gdx.input.isTouched()){
			if (Gdx.input.getX() < GM.cameraDeltaX * GM.ratio) {
				jump();
			}
		}
		if(Gdx.input.isKeyPressed(Keys.Q)){
			jump();
		}
		if(Gdx.input.isKeyPressed(Keys.W)){
			toggleStop();
		}
	}
	
	private void handlePassiveSkill(PassiveSkill skill) {
		switch (skill.getCarac()) {
		case BOW_STRENGTH:
			bow.setStrength(handleBonus(bow.getStrength(), skill.getType(), skill.getBonus()));
			break;
		case SPEED:
			break;
		case TIME_SPEED:
			break;
		case ARROW_COUNT:
			bow.setArrowCount(handleBonus(bow.getArrowCount(), skill.getType(), skill.getBonus()));
			break;
		default:
			break;
		}
	}

	private float handleBonus(float val, ModifType type, float bonus) {
		float res = val;
		switch (type) {
		case ADD:
			res = val + bonus;
			break;
		case DIV:
			res = val / bonus;
			break;
		case MUL:
			res = val * bonus;
			break;
		case SUB:
			res = val - bonus;
			break;
		}
		return res;
	}

	/**
	 * Set the chicken animation to run
	 */
	public void run() {
		setAnim("run", true);
		chicken.setLinearDamping(0);
	}

	/**
	 * Set the chicken animation to idle
	 */
	public void idle() {
		setAnim("idle", true);
		chicken.setLinearDamping(10);
	}

	public void jump() {
		if (!jumping && MathUtils.isZero(chicken.getLinearVelocity().y)) {
			jumping = true;
			setAnim("jump", true);
			chicken.applyForce(jumpVector.x, jumpVector.y, width * .5f, 0, true);
		}
	}

	public void onCollideGround() {
		if (jumping) {
			jumping = false;
			setAnim("run", true);
		}
	}

	public void die() {
		chicken.setLinearDamping(10);
		setAnim("idle", true);
	}

	private void setAnim(String anim, boolean loop) {
		Box2dSkeletonBasic skeleton = (Box2dSkeletonBasic) chickenEntity.get(Box2dSkeletonBasic.name);
		skeleton.setAnim(anim, loop);
	}

	/**
	 * Admin method to totaly stop the chicken
	 */
	public void stop() {
		speed = 0;
		chicken.setLinearDamping(10);
		chicken.setLinearVelocity(0,0);
		idle();
	}

	/**
	 * Adminmethod to restart the chicken run
	 */
	public void play() {
		speed = 1;
		run();
	}

	public void toggleStop() {
		if (speed == 0) {
			play();
		} else {
			stop();
		}
	}

	public float getSpeedX() {
		return chicken.getLinearVelocity().x;
	}

	public float getSpeedY() {
		return chicken.getLinearVelocity().y;
	}

	public Bow getBow() {
		return bow;
	}

	public void setBow(Bow bow) {
		this.bow = bow;
	}

	public float getX() {
		return chicken.getPosition().x;
	}

	public float getY() {
		return chicken.getPosition().y;
	}

	public boolean isDead() {
		return chickenEntity.isDead();
	}

	public boolean isJumping() {
		return jumping;
	}

	public Body getChicken() {
		return chicken;
	}

	public void setChicken(Body chicken) {
		this.chicken = chicken;
	}

	public float getWidth() {
		return width;
	}

	public void setWidth(float width) {
		this.width = width;
	}

	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		this.height = height;
	}

	public float getTimeFactor() {
		return timeFactor;
	}

	public void setTimeFactor(float timeFactor) {
		this.timeFactor = timeFactor;
	}
}
