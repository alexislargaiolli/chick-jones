package fr.alex.games.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool.PooledEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.esotericsoftware.spine.SkeletonRenderer;

import fr.alex.games.GM;
import fr.alex.games.Utils;

public class Arrow extends SimpleSpatial {
	boolean dead;
	boolean dieing;
	Vector2 size;
	float timeBeforeDeath = -1;
	PooledEffect effect;
	int contactCount = 0;
	int maxContact = 1;

	public Arrow(Body body, float rotationInDegrees, Vector2 size, Vector2 center, TextureRegion region, Texture diffuse, Texture normal, PooledEffect effect) {
		super(region, diffuse, normal, false, body, Color.WHITE, size, center, rotationInDegrees);
		mSprite.setSize(size.x, size.y);
		// mSprite.setOrigin(size.x*.5f, size.y);
		mSprite.setScale(1, 1.6f);
		this.effect = effect;
		dieing = false;
		dead = false;
		this.size = size;
	}

	public void render(SkeletonRenderer skeletonRenderer, SpriteBatch batch, float delta) {
		if (dieing) {
			timeBeforeDeath -= delta;
			if (timeBeforeDeath < 0) {
				dead = true;
			}
		}
		if (effect != null) {
			effect.setPosition(mBody.getPosition().x * Utils.BOX_WORLD_TO, mBody.getPosition().y * Utils.BOX_WORLD_TO);
		}
		super.render(skeletonRenderer, batch, delta);
	}

	public Vector2 getHead() {
		return mBody.getWorldPoint(new Vector2(size.x * .5f, 0));
	}

	public Vector2 getTail() {
		return mBody.getWorldPoint(new Vector2(size.x * -.5f, 0));
	}

	public static Body createBody(float x, float y, float width, float height) {
		PolygonShape shape = new PolygonShape();

		// Polygon arrow
		// shape.set(new float[] { width * -.5f, 0, width * .25f, height * .5f,
		// width * .5f, 0, width * .25f, -height * .5f });

		// Rectangle
		shape.set(new float[] { -width * .5f, -height * .5f, -width * .5f, height * .5f, width * .5f, height * .5f, width * .5f, -height * .5f });

		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.position.set(x, y);
		bodyDef.allowSleep = false;
		bodyDef.gravityScale = 1f;
		Body body = GM.world.createBody(bodyDef);

		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = shape;
		fixtureDef.density = 2f;
		fixtureDef.restitution = .5f;
		fixtureDef.friction = .5f;
		body.createFixture(fixtureDef);
		return body;
	}

	public PooledEffect getEffect() {
		return effect;
	}

	public void setEffect(PooledEffect effect) {
		this.effect = effect;
	}

	public void setTimeBeforeDeath(float timeBeforeDeath) {
		if (!dieing) {
			dieing = true;
			this.timeBeforeDeath = timeBeforeDeath;
		}
	}

	public void contact() {
		contactCount++;
	}

	public boolean maxContactReached() {
		return contactCount == maxContact;
	}

	public boolean isDead() {
		return dead;
	}

	public void setDead(boolean dead) {
		this.dead = dead;
	}

	public int getContactCount() {
		return contactCount;
	}

	public void setContactCount(int contactCount) {
		this.contactCount = contactCount;
	}

	public int getMaxContact() {
		return maxContact;
	}

	public void setMaxContact(int maxContact) {
		this.maxContact = maxContact;
	}

}
