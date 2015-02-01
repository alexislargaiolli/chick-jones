package fr.alex.games.box2d.entities.components;

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

import fr.alex.games.AM;
import fr.alex.games.GM;
import fr.alex.games.box2d.entities.Component;
import fr.alex.games.box2d.entities.ComponentEvent;
import fr.alex.games.box2d.entities.Entity;
import fr.alex.games.box2d.entities.EventType;

public class Box2dSkeleton extends Component {
	public final static String name = "skeleton";
	protected boolean toDestroy;
	private Skeleton skeleton;
	private AnimationStateData stateData;
	private AnimationState animState;
	private Bone root;

	public Box2dSkeleton(Entity entity, String spineFile, Body body, Vector2 size) {
		super(entity);
		entity.addListner(this);
		entity.setPosition(body.getPosition().cpy());
		final TextureAtlas atlas = AM.getSceneAtlas();
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
		stateData = new AnimationStateData(skeletonData);
		stateData.setMix("idle", "idle", 0.2f);
		animState = new AnimationState(stateData);
		animState.setAnimation(0, "idle", true);

		skeleton = new Skeleton(skeletonData);
		root = skeleton.findBone("root");
		root.setScale(scale, scale);
		root.setPosition(entity.getPosition().x, entity.getPosition().y);
		skeleton.updateWorldTransform();

		Vector2 vector = new Vector2();
		for (Slot slot : skeleton.getSlots()) {
			if (!(slot.getAttachment() instanceof Box2dAttachment))
				continue;
			Box2dAttachment attachment = (Box2dAttachment) slot.getAttachment();
			PolygonShape boxPoly = new PolygonShape();
			float width = attachment.getWidth() / 3 * attachment.getScaleX() * scale;
			float height = attachment.getHeight() / 3 * attachment.getScaleY() * scale;
			Vector2 c = vector.set(attachment.getX() * scale, attachment.getY() * scale);
			float angle = attachment.getRotation() * MathUtils.degRad;
			boxPoly.setAsBox(width, height, c, angle);
			BodyDef boxBodyDef = new BodyDef();
			boxBodyDef.type = BodyType.StaticBody;
			attachment.body = GM.scene.getWorld().createBody(boxBodyDef);
			attachment.body.createFixture(boxPoly, 1);
			attachment.body.setUserData(entity);
			boxPoly.dispose();
		}
		GM.scene.getWorld().destroyBody(body);
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
			root.setPosition(entity.getPosition().x, entity.getPosition().y);
			animState.update(delta);
			animState.apply(skeleton);
			skeleton.updateWorldTransform();
			updateSlotPosition();
		}
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

	public void flipX() {
		skeleton.setFlip(!skeleton.getFlipX(), skeleton.getFlipY());
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
