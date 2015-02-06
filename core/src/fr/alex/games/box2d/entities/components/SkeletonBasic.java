package fr.alex.games.box2d.entities.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.spine.AnimationState;
import com.esotericsoftware.spine.AnimationStateData;
import com.esotericsoftware.spine.Bone;
import com.esotericsoftware.spine.Skeleton;
import com.esotericsoftware.spine.SkeletonData;
import com.esotericsoftware.spine.SkeletonJson;
import com.esotericsoftware.spine.SkeletonRenderer;

import fr.alex.games.AM;
import fr.alex.games.box2d.entities.Component;
import fr.alex.games.box2d.entities.Entity;

public class SkeletonBasic extends Component {
	public final static String name = "spineskeleton";
	protected final Vector2 center = new Vector2();
	protected final Vector2 halfSize = new Vector2();
	private Skeleton skeleton;
	private AnimationStateData stateData;
	private AnimationState animState;
	private Bone root;
	protected Vector2 tmp = new Vector2();
	private float angle;

	public SkeletonBasic(Entity entity, String spineFile, Vector2 size, Vector2 center, float angle) {
		super(entity);
		this.halfSize.set(size.x / 2, size.y / 2);
		this.center.set(center);
		this.angle = angle;
		SkeletonJson skeletonJson = new SkeletonJson(AM.getSpineAtlas());
		SkeletonData skeletonData = skeletonJson.readSkeletonData(Gdx.files.internal(spineFile + ".json"));
		float scale = size.y / skeletonData.getHeight();
		stateData = new AnimationStateData(skeletonData);
		animState = new AnimationState(stateData);

		skeleton = new Skeleton(skeletonData);
		root = skeleton.findBone("root");
		root.setScale(scale, scale);
		root.setPosition(center.x, center.y);

		updatePosition((float) Math.random() * 2f);
	}

	private void updatePosition(float delta) {
		tmp.set(center).rotate(angle).add(entity.getPosition()).sub(halfSize);

		root.setRotation(angle * (skeleton.getFlipX() ? -1 : 1));
		skeleton.setPosition(entity.getPosition().x - center.x, entity.getPosition().y - center.y);

		animState.update(delta);
		animState.apply(skeleton);

		skeleton.updateWorldTransform();
	}

	@Override
	public void update(float delta) {
		updatePosition(delta);
	}

	@Override
	public void draw(SpriteBatch batch, SkeletonRenderer skeletonRenderer, float delta) {
		skeletonRenderer.draw(batch, skeleton);
	}

	public void addMix(String anim1, String anim2, float duration) {
		animState.getData().setMix(anim1, anim2, duration);
	}

	public void setAnim(String anim, boolean loop) {
		animState.setAnimation(0, anim, loop);
	}

	public void setAnim(int trackIndex, String anim, boolean loop) {
		animState.setAnimation(trackIndex, anim, loop);
	}
	
	public void addAnim(int trackIndex, String anim, boolean loop, float delay) {
		animState.addAnimation(trackIndex, anim, loop, delay);
	}

	public void idle() {
		animState.setAnimation(0, "idle", true);
	}

	public void flipX() {
		skeleton.setFlip(!skeleton.getFlipX(), skeleton.getFlipY());
	}

	@Override
	public String getName() {
		return name;
	}

	public float getAngle() {
		return angle;
	}

	public void setAngle(float angle) {
		this.angle = angle;
	}

	public AnimationState getAnimState() {
		return animState;
	}
}
