package fr.alex.games.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.esotericsoftware.spine.SkeletonRenderer;

import fr.alex.games.AM;
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
	private enum ANIM {
		IDLE("idle"), RUN("run"), JUMP("jump");
		String name;

		ANIM(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}
	}

	private int footContactCount = 0;
	private int jumpCount = 0;

	private float speed = 10;
	private Body body;
	private boolean jumping;
	private boolean stop;

	private float timeFactor = 1;
	private Bow bow;

	private float width = 1, height = 1.2f;

	private Entity chickenEntity;
	private ANIM currentAnim;

	public Chicken(Body chicken) {
		// this.body = chicken;
		createBody(0, 0);

		chickenEntity = new Entity();

		chickenEntity.add(new NormalMap(chickenEntity, AM.getSpineDiffuse(), AM.getSpineNormal()));
		Box2dSkeletonBasic skeleton = new Box2dSkeletonBasic(chickenEntity, "chicken/chicken", this.body, new Vector2(width, height), new Vector2(0, height * .5f));
		skeleton.addMix("idle", "run", 0.4f);
		skeleton.addMix("run", "jump", 0.1f);
		skeleton.addMix("jump", "run", 0.1f);
		chickenEntity.add(skeleton);
		chickenEntity.add(new Dieable(chickenEntity));
		chickenEntity.add(new Collector(chickenEntity));

		bow = new Bow(this, new Vector2(1, 1f));

		for (Item item : ItemManager.get().getEquiped()) {
			for (PassiveSkill skill : item.getPassives()) {
				handlePassiveSkill(skill);
			}
		}
	}

	private void createBody(float x, float y) {
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.position.set(x, y);
		bodyDef.allowSleep = false;
		bodyDef.gravityScale = 1f;
		bodyDef.fixedRotation = true;
		body = GM.scene.getWorld().createBody(bodyDef);

		PolygonShape shape = new PolygonShape();
		shape.setAsBox(0.2f, 0.6f);
		// float[] vertives = new float[] { -1, 0, widthArrow * .2f,
		// -heightArrow * .5f, widthArrow * .5f, 0, widthArrow * .2f,
		// heightArrow * .5f };
		// shape.set(vertives);
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = shape;
		fixtureDef.density = 1f;
		fixtureDef.restitution = 0f;
		fixtureDef.friction = 0f;
		fixtureDef.filter.groupIndex = -1;
		body.createFixture(fixtureDef);

		shape.setAsBox(0.1f, 0.2f, new Vector2(0, -.6f), 0);
		fixtureDef.isSensor = true;
		Fixture f = body.createFixture(fixtureDef);
		f.setUserData(this);
	}

	public void update(State state, float delta) {
		chickenEntity.update(delta);
		bow.setPosition(chickenEntity.getPosition().x + width * 0.3f, chickenEntity.getPosition().y + height * .2f);
		bow.update(state, delta * timeFactor);

		if (state == State.PLAYING && !isDead()) {

			// if (jumping) {
			// if (jumpTimeout == 0) { // jumpFactor *= 0.7f;
			// body.applyLinearImpulse(0, body.getMass() * 5,
			// body.getWorldCenter().x, body.getWorldCenter().y, true);
			// body.applyForce(0, body.getMass() * jumpFactor,
			// body.getWorldCenter().x, body.getWorldCenter().y, true);
			// jumpTimeout = 15;
			// } else {
			// body.applyForce(0, body.getMass() * jumpFactor * 0.1f,
			// body.getWorldCenter().x, body.getWorldCenter().y, true);
			// }
			// } else if (body.getLinearVelocity().y < 3) { // speed =
			// defaultSpeed
			// // * .1f;
			// body.applyForce(0, body.getMass() * -20f,
			// body.getWorldCenter().x, body.getWorldCenter().y, true);
			// } else {
			// speed = defaultSpeed;
			// }
			if (body.getLinearVelocity().y < 3 && footContactCount == 0) {
				body.applyForce(0, body.getMass() * -30f, body.getWorldCenter().x, body.getWorldCenter().y, true);
			}
			if (body.getLinearVelocity().x < 5 && !stop) {
				body.applyForce(speed, 0, body.getWorldCenter().x, body.getWorldCenter().y, true);
			}
			if (footContactCount > 0) {
				if (speed > 0 && !stop) {
					setAnim(ANIM.RUN, true);
				} else {
					setAnim(ANIM.IDLE, true);
				}
			} else {
				setAnim(ANIM.JUMP, true);
			}
		}
		if(state == State.ENDING){
			body.setLinearDamping(10);
			setAnim(ANIM.IDLE, true);
		}
	}

	public void jump() {
		if (footContactCount < 1 && jumpCount > 1) {
			return;
		}
		body.applyLinearImpulse(0, body.getMass() * (5.5f - body.getLinearVelocity().y), body.getWorldCenter().x, body.getWorldCenter().y, true);
		jumpCount++;
		if (!jumping) {
			jumpCount = 1;
		}
		jumping = true;
	}

	public void draw(SpriteBatch batch, SkeletonRenderer skeletonRenderer, float delta) {
		chickenEntity.draw(batch, skeletonRenderer, delta);
		bow.draw(batch, skeletonRenderer, delta);
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

	public void die() {
		body.setLinearDamping(10);
		setAnim(ANIM.IDLE, true);
	}

	private void setAnim(ANIM anim, boolean loop) {
		if (!anim.equals(currentAnim)) {
			Box2dSkeletonBasic skeleton = (Box2dSkeletonBasic) chickenEntity.get(Box2dSkeletonBasic.name);
			skeleton.setAnim(anim.getName(), loop);
			currentAnim = anim;
		}
	}

	public void toggleStop() {
		stop = !stop;
		if (stop) {
			body.setLinearVelocity(0, 0);
		}
	}

	public void incrementFootContact() {
		footContactCount++;
		if (footContactCount > 0) {
			jumpCount = 0;
		}
	}

	public void decrementFootContact() {
		footContactCount--;
	}

	public float getSpeedX() {
		return body.getLinearVelocity().x;
	}

	public float getSpeedY() {
		return body.getLinearVelocity().y;
	}

	public Bow getBow() {
		return bow;
	}

	public void setBow(Bow bow) {
		this.bow = bow;
	}

	public float getX() {
		return body.getPosition().x;
	}

	public float getY() {
		return body.getPosition().y;
	}

	public boolean isDead() {
		return chickenEntity.isDead();
	}

	public boolean isJumping() {
		return jumping;
	}

	public Body getChicken() {
		return body;
	}

	public void setChicken(Body chicken) {
		this.body = chicken;
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

	public int getFootContactCount() {
		return footContactCount;
	}

	public void setFootContactCount(int footContactCount) {
		this.footContactCount = footContactCount;
	}
}
