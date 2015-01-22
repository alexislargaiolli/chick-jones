package fr.alex.games.box2d.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.esotericsoftware.spine.AnimationState;
import com.esotericsoftware.spine.AnimationStateData;
import com.esotericsoftware.spine.Bone;
import com.esotericsoftware.spine.Skeleton;
import com.esotericsoftware.spine.SkeletonData;
import com.esotericsoftware.spine.SkeletonJson;
import com.esotericsoftware.spine.SkeletonRenderer;
import com.esotericsoftware.spine.Skin;
import com.esotericsoftware.spine.Slot;
import com.esotericsoftware.spine.attachments.AtlasAttachmentLoader;
import com.esotericsoftware.spine.attachments.RegionAttachment;

import fr.alex.games.GM;

public class SkeletonComponent extends Component {
	public final static String name = "skeleton";
	private Skeleton skeleton;
	private AnimationStateData stateData;
	private AnimationState animState;
	private Vector2 halfSize;

	public SkeletonComponent(Entity entity, String spineFile, Vector2 position, Vector2 size) {
		super(entity);
		GM.assetManager.load(spineFile + ".atlas", TextureAtlas.class);
		GM.assetManager.finishLoading();
		final TextureAtlas atlas = GM.assetManager.get(spineFile + ".atlas", TextureAtlas.class);
		AtlasAttachmentLoader atlasLoader = new AtlasAttachmentLoader(atlas) {

			@Override
			public RegionAttachment newRegionAttachment(Skin skin, String name, String path) {
				Box2dAttachment attachment = new Box2dAttachment(name);
				AtlasRegion region = atlas.findRegion(attachment.getName());
				if (region == null)
					throw new RuntimeException("Region not found in atlas: " + attachment);
				attachment.setRegion(region);
				return attachment;
			}
		};

		SkeletonJson skeletonJson = new SkeletonJson(atlasLoader);
		SkeletonData skeletonData = skeletonJson.readSkeletonData(Gdx.files.internal(spineFile + ".json"));
		float scale = size.y / skeletonData.getHeight();
		halfSize = new Vector2(size.x * .5f, size.y * .5f);
		stateData = new AnimationStateData(skeletonData);
		stateData.setMix("idle", "idle", 0.2f);
		animState = new AnimationState(stateData);
		animState.setAnimation(0, "idle", true);

		skeleton = new Skeleton(skeletonData);
		Bone root = skeleton.findBone("root");
		root.setScale(scale, scale);
		root.setPosition(position.x, position.y);
		skeleton.updateWorldTransform();

		Vector2 vector = new Vector2();
		for (Slot slot : skeleton.getSlots()) {
			if (!(slot.getAttachment() instanceof Box2dAttachment))
				continue;
			Box2dAttachment attachment = (Box2dAttachment) slot.getAttachment();
			PolygonShape boxPoly = new PolygonShape();
			float width = attachment.getWidth() / 2 * attachment.getScaleX() * scale;
			float height = attachment.getHeight() / 2 * attachment.getScaleY() * scale;
			Vector2 c = vector.set(attachment.getX() * scale, attachment.getY() * scale);
			float angle = attachment.getRotation() * MathUtils.degRad;
			boxPoly.setAsBox(width, height, c, angle);
			BodyDef boxBodyDef = new BodyDef();
			boxBodyDef.type = BodyType.DynamicBody;
			attachment.body = GM.scene.getWorld().createBody(boxBodyDef);
			attachment.body.createFixture(boxPoly, 1);
			attachment.body.setUserData(entity);
			boxPoly.dispose();
		}
	}

	@Override
	public void update(float delta) {
		animState.update(delta);
		animState.apply(skeleton);
		skeleton.updateWorldTransform();
		updateSlotPosition();
		entity.setPosition(skeleton.getX() + halfSize.x, skeleton.getY() + halfSize.y);
	}

	@Override
	public void draw(SpriteBatch batch, SkeletonRenderer skeletonRenderer, float delta) {
		skeletonRenderer.draw(batch, skeleton);
	}

	@Override
	public void contact(Entity entity, Contact contact) {

	}

	public void idle() {
		animState.setAnimation(0, "idle", true);
	}

	public void destroy() {
		for (Slot slot : skeleton.getSlots()) {
			if (!(slot.getAttachment() instanceof Box2dAttachment))
				continue;
			Box2dAttachment attachment = (Box2dAttachment) slot.getAttachment();
			if (attachment.body == null)
				continue;
			GM.scene.getWorld().destroyBody(attachment.body);
			// attachment.body = null;
		}
	}

	private void updateSlotPosition() {
		for (Slot slot : skeleton.getSlots()) {
			if (!(slot.getAttachment() instanceof Box2dAttachment))
				continue;
			Box2dAttachment attachment = (Box2dAttachment) slot.getAttachment();
			if (attachment.body == null)
				continue;
			attachment.body.setTransform(slot.getBone().getWorldX(), slot.getBone().getWorldY(), slot.getBone().getWorldRotation() * MathUtils.degRad);
		}
	}

	static class Box2dAttachment extends RegionAttachment {
		Body body;

		public Box2dAttachment(String name) {
			super(name);
		}
	}

	@Override
	public String getName() {
		return name;
	}
}
