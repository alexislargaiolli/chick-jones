package fr.alex.games.box2d.entities.components;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.joints.WeldJointDef;
import com.esotericsoftware.spine.SkeletonRenderer;

import fr.alex.games.GM;
import fr.alex.games.StickyInfo;
import fr.alex.games.box2d.entities.Component;
import fr.alex.games.box2d.entities.ComponentEvent;
import fr.alex.games.box2d.entities.Entity;
import fr.alex.games.box2d.entities.EventType;
import fr.alex.games.box2d.entities.PhysicListener;
import fr.alex.games.entity.Bow;

public class Stick extends Component implements PhysicListener {
	public final static String name = "stick";
	private boolean sticked;
	private StickyInfo stickInfo = null;
	private Body body;
	private boolean canStick;

	public Stick(Entity entity) {
		super(entity);
		entity.addListner(this);
		body = ((Box2dSprite) entity.get(Box2dSprite.name)).getBody();
	}

	@Override
	public void update(float delta) {
		if (stickInfo != null) {
			WeldJointDef def = new WeldJointDef();
			def.initialize(stickInfo.getB1(), stickInfo.getB2(), stickInfo.getAnchor());
			GM.scene.getWorld().createJoint(def);
			stickInfo = null;
		}
	}

	@Override
	public void draw(SpriteBatch batch, SkeletonRenderer skeletonRenderer, float delta) {

	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void beginContact(Entity other, Contact contact) {
		Vector2 pointingDirection = body.getWorldPoint(new Vector2(Bow.widthArrow * 0.5f, 0));
		Vector2 contactPoint = contact.getWorldManifold().getPoints()[0];
		float dst = pointingDirection.dst(contactPoint);
		canStick = dst < 0.1 && other.contains(Stickable.name);
	}

	@Override
	public void endContact(Entity other, Contact contact) {

	}

	@Override
	public void preSolve(Entity other, Contact contact, Manifold oldManifold) {
		if (canStick) {
			contact.setEnabled(false);
		}
		if (canStick && !sticked && contact.isTouching()) {
			Vector2 pointingDirection = body.getWorldPoint(new Vector2(0.3f, 0));
			Vector2 contactPoint = contact.getWorldManifold().getPoints()[0];
			float dst = pointingDirection.dst(contactPoint);

			if (dst > 0.05) {
				stick(other, contact);
			}
		}
	}

	@Override
	public void postSolve(Entity other, Contact contact, ContactImpulse impulse) {

	}

	private void stick(Entity other, Contact contact) {
		Vector2 contactPoint = contact.getWorldManifold().getPoints()[0];
		Body stickBody = contact.getFixtureA().getBody();
		Body stickableBody = contact.getFixtureB().getBody();

		stickInfo = new StickyInfo(stickBody, stickableBody, contactPoint);
		sticked = true;
		other.broadcastEvent(new ComponentEvent(this, null, EventType.ARROW_STICK));
		Vector2 speed = new Vector2(body.getLinearVelocity());
		speed.scl(-1);
		body.applyForceToCenter(speed, true);
	}

}
