package fr.alex.games.box2d.entities.components;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.joints.WeldJointDef;
import com.esotericsoftware.spine.SkeletonRenderer;

import fr.alex.games.GM;
import fr.alex.games.StickyInfo;
import fr.alex.games.box2d.entities.Component;
import fr.alex.games.box2d.entities.ComponentEvent;
import fr.alex.games.box2d.entities.Entity;
import fr.alex.games.box2d.entities.EventType;

public class Stick extends Component {
	public final static String name = "stick";
	private boolean sticked;
	private StickyInfo stickInfo = null;

	public Stick(Entity entity) {
		super(entity);
		entity.addListner(this);
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
	public void contact(Entity other, Contact contact) {
		if (!sticked && other.contains(Stickable.name)) {
			Vector2 contactPoint = contact.getWorldManifold().getPoints()[0];
			contact.setEnabled(false);

			Body stickBody = contact.getFixtureA().getBody();
			Body stickableBody = contact.getFixtureB().getBody();

			stickBody.setLinearDamping(10);
			stickInfo = new StickyInfo(stickBody, stickableBody, contactPoint);
			sticked = true;
			other.broadcastEvent(new ComponentEvent(this, null, EventType.ARROW_STICK));
		}
	}

	@Override
	public String getName() {
		return name;
	}

}
