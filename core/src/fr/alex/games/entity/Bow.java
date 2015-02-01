package fr.alex.games.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool.PooledEffect;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;
import com.esotericsoftware.spine.Animation;
import com.esotericsoftware.spine.BoneData;
import com.esotericsoftware.spine.Skeleton;
import com.esotericsoftware.spine.SkeletonData;
import com.esotericsoftware.spine.SkeletonJson;
import com.esotericsoftware.spine.SkeletonRenderer;

import fr.alex.games.AM;
import fr.alex.games.GM;
import fr.alex.games.box2d.entities.Component;
import fr.alex.games.box2d.entities.Entity;
import fr.alex.games.box2d.entities.components.ArrowRotation;
import fr.alex.games.box2d.entities.components.Box2dSprite;
import fr.alex.games.box2d.entities.components.Collector;
import fr.alex.games.box2d.entities.components.Destroyer;
import fr.alex.games.box2d.entities.components.NormalMap;
import fr.alex.games.box2d.entities.components.Stick;
import fr.alex.games.screens.GameScreen.State;

public class Bow {
	/**
	 * Default shoot strength
	 */
	private float strength;

	private boolean bend = false;

	private float bendSize = 0f;

	private Vector2 origin;

	private Vector2 velocity;

	private Skeleton skeleton;
	private Animation animShot;
	private Animation animBend;
	private Animation currentAnim;
	private BoneData root;
	private float animTime = 0, animDuration = .2f;
	private float angle;
	private TextureRegion arrowTexture;

	private PooledEffect effect;

	float widthArrow = .6f;
	float heightArrow = .06f;
	float arrowCount = 1;
	private Sprite arrowSprite;
	NormalMap normalMap;

	public Bow(Chicken player, Vector2 size) {
		super();
		normalMap = new NormalMap(null, AM.getSpineDiffuse(), AM.getSpineNormal());
		origin = new Vector2();
		velocity = new Vector2();
		strength = 10f;

		SkeletonJson skeletonJson = new SkeletonJson(AM.getSpineAtlas());
		SkeletonData skeletonData = skeletonJson.readSkeletonData(Gdx.files.internal("chicken/bow.json"));
		root = skeletonData.findBone("root");
		float scale = size.y / skeletonData.getHeight();
		root.setScale(scale, scale);
		root.setRotation(30);

		animShot = skeletonData.findAnimation("shot");
		animBend = skeletonData.findAnimation("bend");

		skeleton = new Skeleton(skeletonData);
		arrowSprite = new Sprite();
		arrowTexture = AM.getSpineAtlas().findRegion("arrow");
		bend();
	}

	public void shot() {

	}

	public void update(State state, float delta) {

		if (currentAnim != null && animTime <= animDuration) {
			currentAnim.apply(skeleton, animTime - delta, animTime, false, null);
			animTime += delta;
			if (animTime >= animDuration) {
				if (currentAnim.equals(animShot)) {
					bend();
				}
			}
		}

		skeleton.getRootBone().setRotation(angle);
		skeleton.setPosition(origin.x, origin.y);
		skeleton.updateWorldTransform();

		if (bend) {
			if (bendSize <= 1) {
				bendSize += delta;
			}
		}
	}

	public void draw(SpriteBatch batch, SkeletonRenderer skeletonRenderer) {
		normalMap.draw(batch, skeletonRenderer, 0);
		skeletonRenderer.draw(batch, skeleton);
		if (bend) {
			bendSize = Math.min(1, (animTime / animDuration));
			if (arrowCount > 1) {
				for (int i = 0; i < arrowCount; ++i) {
					float x = origin.x;
					float y = origin.y;
					float spaceBetweenArrow = .3f / arrowCount;
					x += MathUtils.cosDeg(angle + 90) * (i * spaceBetweenArrow - (arrowCount - 1) * .5f * spaceBetweenArrow);
					y += MathUtils.sinDeg(angle + 90) * (i * spaceBetweenArrow - (arrowCount - 1) * .5f * spaceBetweenArrow);

					arrowSprite.setPosition(x - MathUtils.cosDeg(angle) * (widthArrow * bendSize), y - MathUtils.sinDeg(angle) * (widthArrow * bendSize));
					arrowSprite.setRotation(angle);
					arrowSprite.draw(batch);
				}
			} else {
				arrowSprite.setPosition(origin.x - MathUtils.cosDeg(angle) * (widthArrow * bendSize), origin.y - MathUtils.sinDeg(angle) * (widthArrow * bendSize));
				arrowSprite.setRotation(angle);
				arrowSprite.draw(batch);
			}
		}
	}

	public float getArrowCount() {
		return arrowCount;
	}

	public void setArrowCount(float arrowCount) {
		this.arrowCount = arrowCount;
	}

	public Vector2 computeVelocity() {
		return velocity.set(strength * (float) Math.cos(getAngleRad()), strength * (float) Math.sin(getAngleRad()));
	}

	/**
	 * Start bending of the bow
	 */
	public void bend() {
		bend = true;
		bendSize = 0;
		animTime = 0;
		currentAnim = animBend;
		arrowSprite.setRegion(arrowTexture);
		arrowSprite.setScale(1, 1.6f);
		arrowSprite.setSize(widthArrow, heightArrow);
		arrowSprite.setPosition(origin.x, origin.y);
		arrowSprite.setRotation(angle);
	}

	/**
	 * Release bend and shot an arrow
	 * 
	 * @param region
	 * @param effect
	 * @return
	 */
	public Array<Entity> fire() {
		Array<Entity> arrows = new Array<Entity>();
		if (arrowCount > 1) {
			for (int i = 0; i < arrowCount; ++i) {
				float x = origin.x;
				float y = origin.y;
				float j = i - arrowCount * .5f;
				if (j < arrowCount * .5f) {
					x -= MathUtils.cosDeg(angle + 90) * j * .05f;
					y -= MathUtils.sinDeg(angle + 90) * j * .05f;
				} else if (j > arrowCount * .5f) {
					x += MathUtils.cosDeg(angle + 90) * j * .05f;
					y += MathUtils.sinDeg(angle + 90) * j * .05f;
				}
				x = x - MathUtils.cosDeg(angle) * widthArrow * .5f;
				y = y - MathUtils.sinDeg(angle) * widthArrow * .5f;

				Entity arrow = createArrow(x, y);
				arrows.add(arrow);
			}
		} else {
			Entity arrow = createArrow(origin.x, origin.y);
			arrows.add(arrow);
		}
		bend = false;
		animTime = 0;
		currentAnim = animShot;
		return arrows;
	}

	private Entity createArrow(float x, float y) {
		PolygonShape shape = new PolygonShape();

		// float[] vertives = new float[] { -widthArrow * .5f, -heightArrow *
		// .5f, -widthArrow * .5f, heightArrow * .5f, widthArrow * .5f,
		// heightArrow * .5f, widthArrow * .5f, -heightArrow * .5f };
		float[] vertives = new float[] { -widthArrow * .5f, 0, widthArrow * .2f, -heightArrow * .5f, widthArrow * .5f, 0, widthArrow * .2f, heightArrow * .5f };

		shape.set(vertives);

		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.position.set(x, y);
		bodyDef.allowSleep = false;
		bodyDef.gravityScale = 1f;
		Body body = GM.world.createBody(bodyDef);

		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = shape;
		fixtureDef.density = 1f;
		fixtureDef.restitution = .8f;
		fixtureDef.friction = .1f;
		fixtureDef.filter.groupIndex = -1;
		body.createFixture(fixtureDef);

		body.setTransform(x, y, getAngleRad());

		body.setLinearVelocity(computeVelocity().cpy());
		body.setAngularDamping(1f);

		/*
		 * Arrow arrow = new Arrow(body, getAngleRad(), new Vector2(widthArrow,
		 * heightArrow), new Vector2(), arrowTexture, diffuse, normal, effect);
		 * body.setUserData(arrow);
		 */
		Entity arrow = new Entity();
		Component c = new Box2dSprite(arrow, arrowTexture, false, body, Color.WHITE, new Vector2(widthArrow*1.2f, heightArrow*1.8f), Vector2.Zero, 0);
		arrow.add(c);
		arrow.add(new ArrowRotation(arrow));
		arrow.add(new Destroyer(arrow));
		arrow.add(new Stick(arrow));		
		arrow.add(new Collector(arrow));
		return arrow;
	}

	public float getAngleRad() {
		return MathUtils.degreesToRadians * angle;
	}

	public void setOrigin(float x, float y) {
		this.origin.x = x;
		this.origin.y = y;
	}

	public float getStrength() {
		return strength;
	}

	public void setStrength(float strength) {
		this.strength = strength;
	}

	public Vector2 getOrigin() {
		return origin;
	}

	public void setOrigin(Vector2 origin) {
		this.origin = origin;
	}

	public Vector2 getVelocity() {
		return velocity;
	}

	public void setVelocity(Vector2 velocity) {
		this.velocity = velocity;
	}

	public float getAngle() {
		return angle;
	}

	public void setAngle(float angle) {
		this.angle = angle;
	}

	public boolean isBend() {
		return bend;
	}

	public float getBendSize() {
		return bendSize;
	}

	public void setBendSize(float bendSize) {
		this.bendSize = bendSize;
	}

	public float getWidthArrow() {
		return widthArrow;
	}

	public void setWidthArrow(float widthArrow) {
		this.widthArrow = widthArrow;
	}

	public float getHeightArrow() {
		return heightArrow;
	}

	public void setHeightArrow(float heightArrow) {
		this.heightArrow = heightArrow;
	}

	public Sprite getArrowSprite() {
		return arrowSprite;
	}

	public void setArrowSprite(Sprite arrowSprite) {
		this.arrowSprite = arrowSprite;
	}

	public TextureRegion getArrowTexture() {
		return arrowTexture;
	}

	public void setArrowTexture(TextureRegion arrowTexture) {
		this.arrowTexture = arrowTexture;
	}

	public PooledEffect getEffect() {
		return effect;
	}

	public void setEffect(PooledEffect effect) {
		this.effect = effect;
	}

}