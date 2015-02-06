package fr.alex.games.entity;

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
import com.esotericsoftware.spine.AnimationState.TrackEntry;
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
import fr.alex.games.box2d.entities.components.SkeletonBasic;
import fr.alex.games.box2d.entities.components.Stick;
import fr.alex.games.screens.GameScreen.State;

public class Bow extends Entity{
	public static float widthArrow = .9f;
	public static float heightArrow = .06f;
	
	/**
	 * Default shoot strength
	 */
	private float strength = 15;
	/**
	 * Current angle of the bow
	 */
	private float angle = 0;
	/**
	 * Current size of bow bending
	 */
	private float bendSize = 0f;
	/**
	 * Number of arrow the bow shot at the same time
	 */
	private float arrowCount = 1;
	
	private Vector2 velocity;
	private TextureRegion arrowTexture;
	private PooledEffect effect;
	private Sprite arrowSprite;
	SkeletonBasic skel;
	TrackEntry bendTrack;
	TrackEntry shotTrack;

	public Bow(Chicken player, Vector2 size) {
		super();
		velocity = new Vector2();
		add(new NormalMap(this, AM.getSpineDiffuse(), AM.getSpineNormal()));
		skel = new SkeletonBasic(this, "chicken/bow", size, new Vector2(size.x * .5f, size.y * .5f), angle);
		skel.addMix("shot", "bend", 0.2f);
		skel.addMix("bend", "shot", 0.2f);
		add(skel);

		arrowSprite = new Sprite();
		arrowTexture = AM.getSpineAtlas().findRegion("arrow");
		arrowSprite.setRegion(arrowTexture);
		arrowSprite.setScale(1, 1.6f);
		arrowSprite.setSize(widthArrow, heightArrow);
		arrowSprite.setPosition(position.x, position.y);
		arrowSprite.setRotation(angle);
		skel.setAnim(1, "bend", false);
	}

	public void update(State state, float delta) {
		super.update(delta);
		skel.setAngle(angle);
		shotTrack = skel.getAnimState().getCurrent(0);
		bendTrack = skel.getAnimState().getCurrent(1);
	}

	public void draw(SpriteBatch batch, SkeletonRenderer skeletonRenderer, float delta) {
		super.draw(batch, skeletonRenderer, delta);
		if(bendTrack != null){			
			bendSize = Math.min(1, (bendTrack.getTime() / bendTrack.getAnimation().getDuration()));			
		}
		if(shotTrack == null){
			if (arrowCount > 1) {
				for (int i = 0; i < arrowCount; ++i) {
					float x = position.x;
					float y = position.y;
					float spaceBetweenArrow = .3f / arrowCount;
					x += MathUtils.cosDeg(angle + 90) * (i * spaceBetweenArrow - (arrowCount - 1) * .5f * spaceBetweenArrow);
					y += MathUtils.sinDeg(angle + 90) * (i * spaceBetweenArrow - (arrowCount - 1) * .5f * spaceBetweenArrow);

					arrowSprite.setPosition(x - MathUtils.cosDeg(angle) * (widthArrow * 0.8f * bendSize), y - MathUtils.sinDeg(angle) * (widthArrow * 0.8f * bendSize));
					arrowSprite.setRotation(angle);
					arrowSprite.draw(batch);
				}
			} else {
				arrowSprite.setPosition(position.x - MathUtils.cosDeg(angle) * (widthArrow * 0.8f * bendSize), position.y - MathUtils.sinDeg(angle) * (widthArrow * 0.8f * bendSize));
				arrowSprite.setRotation(angle);
				arrowSprite.draw(batch);
			}
		}
	}

	public Vector2 computeVelocity() {
		return velocity.set(strength * (float) Math.cos(getAngleRad()), strength * (float) Math.sin(getAngleRad()));
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
				float x = position.x;
				float y = position.y;
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
			Entity arrow = createArrow(position.x, position.y);
			arrows.add(arrow);
		}
		skel.addAnim(0, "shot", false, -1);
		skel.addAnim(1, "bend", false, -1);
		return arrows;
	}

	private Entity createArrow(float x, float y) {
		PolygonShape shape = new PolygonShape();
		float[] vertives = new float[] { -widthArrow * .5f, 0, widthArrow * .2f, -heightArrow * .5f, widthArrow * .5f, 0, widthArrow * .2f, heightArrow * .5f };
		shape.set(vertives);
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.position.set(x, y);
		bodyDef.allowSleep = false;
		//bodyDef.bullet = true;
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

		Entity arrow = new Entity();
		Component c = new Box2dSprite(arrow, arrowTexture, false, body, Color.WHITE, new Vector2(widthArrow*1.2f, heightArrow*1.8f), Vector2.Zero, 0);
		arrow.add(c);
		//arrow.add(new BodyAttachedEffect(arrow, body, Effect.FIRE));
		arrow.add(new ArrowRotation(arrow));
		arrow.add(new Destroyer(arrow));
		arrow.add(new Stick(arrow));		
		arrow.add(new Collector(arrow));
		return arrow;
	}

	public float getAngleRad() {
		return MathUtils.degreesToRadians * angle;
	}

	public float getStrength() {
		return strength;
	}

	public void setStrength(float strength) {
		this.strength = strength;
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

	public float getBendSize() {
		return bendSize;
	}

	public void setBendSize(float bendSize) {
		this.bendSize = bendSize;
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

	public float getArrowCount() {
		return arrowCount;
	}

	public void setArrowCount(float arrowCount) {
		this.arrowCount = arrowCount;
	}

}