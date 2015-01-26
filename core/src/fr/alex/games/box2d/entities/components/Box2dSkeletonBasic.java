package fr.alex.games.box2d.entities.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.esotericsoftware.spine.AnimationState;
import com.esotericsoftware.spine.AnimationStateData;
import com.esotericsoftware.spine.Bone;
import com.esotericsoftware.spine.Skeleton;
import com.esotericsoftware.spine.SkeletonData;
import com.esotericsoftware.spine.SkeletonJson;
import com.esotericsoftware.spine.SkeletonRenderer;

import fr.alex.games.GM;
import fr.alex.games.box2d.entities.Component;
import fr.alex.games.box2d.entities.ComponentEvent;
import fr.alex.games.box2d.entities.Entity;
import fr.alex.games.box2d.entities.EventType;

public class Box2dSkeletonBasic extends Component {
	public final static String name = "skeletonbasic";
	protected boolean toDestroy;
	protected final Vector2 center = new Vector2();
	protected final Vector2 halfSize = new Vector2();
	private Skeleton skeleton;
	private AnimationStateData stateData;
	private AnimationState animState;
	private Bone root;
	private Body body;
	protected Vector2 tmp = new Vector2();

	public Box2dSkeletonBasic(Entity entity, String spineFile, Body body, Vector2 size, Vector2 center) {
		super(entity);
		this.body = body;
		this.halfSize.set(size.x / 2, size.y / 2);
		this.center.set(center);
		this.body.setUserData(entity);
		entity.addListner(this);
		entity.setPosition(body.getPosition().cpy());
		GM.assetManager.load(spineFile + ".atlas", TextureAtlas.class);
		GM.assetManager.finishLoading();
		final TextureAtlas atlas = GM.assetManager.get(spineFile + ".atlas", TextureAtlas.class);

		SkeletonJson skeletonJson = new SkeletonJson(atlas);
		SkeletonData skeletonData = skeletonJson.readSkeletonData(Gdx.files.internal(spineFile + ".json"));
		float scale = size.y / skeletonData.getHeight();
		stateData = new AnimationStateData(skeletonData);
		stateData.setMix("idle", "idle", 0.2f);
		animState = new AnimationState(stateData);
		animState.setAnimation(0, "idle", true);

		skeleton = new Skeleton(skeletonData);
		root = skeleton.findBone("root");
		root.setScale(scale, scale);
		root.setPosition(center.x, center.y);

		updatePosition((float) Math.random() * 2f);
	}

	private void updatePosition(float delta) {
		entity.setPosition(body.getPosition().x, body.getPosition().y);
		float angle = body.getAngle() * MathUtils.radiansToDegrees;
		tmp.set(center).rotate(angle).add(body.getPosition()).sub(halfSize);

		root.setRotation(angle * (skeleton.getFlipX() ? -1 : 1));
		skeleton.setPosition(body.getPosition().x - center.x, body.getPosition().y - center.y);

		animState.update(delta);
		animState.apply(skeleton);

		skeleton.updateWorldTransform();
	}

	@Override
	public void onEvent(ComponentEvent event) {
		if (event.getType() == EventType.DESTROY) {
			toDestroy = true;
		}
	}

	@Override
	public void update(float delta) {
		if (toDestroy && !entity.isToRemove()) {
			destroy();
			this.entity.setToRemove(true);
		} else {
			updatePosition(delta);
		}
	}

	@Override
	public void draw(SpriteBatch batch, SkeletonRenderer skeletonRenderer, float delta) {
		skeletonRenderer.draw(batch, skeleton);
	}

	@Override
	public void contact(Entity entity, Contact contact) {

	}

	public void addMix(String anim1, String anim2, float duration) {
		animState.getData().setMix(anim1, anim2, duration);
	}

	public void setAnim(String anim, boolean loop) {
		animState.setAnimation(0, anim, loop);
	}

	public void idle() {
		animState.setAnimation(0, "idle", true);
	}

	public void destroy() {
		GM.scene.getWorld().destroyBody(body);
	}

	public void flipX() {
		skeleton.setFlip(!skeleton.getFlipX(), skeleton.getFlipY());
	}

	@Override
	public String getName() {
		return name;
	}

	public Body getBody() {
		return body;
	}
}
