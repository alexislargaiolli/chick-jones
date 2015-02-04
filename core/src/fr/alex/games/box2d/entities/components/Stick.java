package fr.alex.games.box2d.entities.components;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.badlogic.gdx.physics.box2d.joints.WeldJointDef;
import com.esotericsoftware.spine.SkeletonRenderer;

import fr.alex.games.GM;
import fr.alex.games.StickyInfo;
import fr.alex.games.box2d.entities.Component;
import fr.alex.games.box2d.entities.ComponentEvent;
import fr.alex.games.box2d.entities.Entity;
import fr.alex.games.box2d.entities.EventType;
import fr.alex.games.box2d.entities.PhysicListener;

public class Stick extends Component implements PhysicListener {
	public final static String name = "stick";
	private boolean sticked;
	private StickyInfo stickInfo = null;
	private Body body;
	private boolean canStick;
	private boolean shouldStick;

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
		contact.setEnabled(false);
		Vector2 pointingDirection = body.getWorldPoint(new Vector2(0.3f, 0));
		Vector2 contactPoint = contact.getWorldManifold().getPoints()[0];
		float dst = pointingDirection.dst(contactPoint);
		if (dst < 0.01) {
			canStick = other.contains(Stickable.name);
		}
	}

	@Override
	public void endContact(Entity other, Contact contact) {

	}

	@Override
	public void preSolve(Entity other, Contact contact, Manifold oldManifold) {
		if (canStick && !sticked && !shouldStick) {
			Vector2 pointingDirection = body.getWorldPoint(new Vector2(0.3f, 0));
			Vector2 contactPoint = contact.getWorldManifold().getPoints()[0];
			float dst = pointingDirection.dst(contactPoint);
			System.out.println(dst);
			if(dst > 0.1){
				shouldStick = true;
				contact.setRestitution(0);
			}
			else{
				contact.setEnabled(false);
			}
		}
	}

	@Override
	public void postSolve(Entity other, Contact contact, ContactImpulse impulse) {
		if (shouldStick && !sticked) {
			Vector2 contactPoint = contact.getWorldManifold().getPoints()[0];
			Body stickBody = contact.getFixtureA().getBody();
			Body stickableBody = contact.getFixtureB().getBody();

			stickInfo = new StickyInfo(stickBody, stickableBody, contactPoint);
			sticked = true;
			other.broadcastEvent(new ComponentEvent(this, null, EventType.ARROW_STICK));
		}
	}

}
