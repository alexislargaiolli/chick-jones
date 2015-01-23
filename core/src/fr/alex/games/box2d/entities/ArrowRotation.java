package fr.alex.games.box2d.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.esotericsoftware.spine.SkeletonRenderer;

public class ArrowRotation extends Component{

	public final static String name = "rotation";
	
	/**
	 * Drag constant for arrow rotation
	 */
	private static final float dragConstant = 3f;
	
	public ArrowRotation(Entity entity) {
		super(entity);		
	}

	@Override
	public void update(float delta) {
		Body body = ((SpriteComponent) entity.get(SpriteComponent.name)).getBody();
		float flightSpeed = new Vector2(body.getLinearVelocity()).nor().len();
		float bodyAngle = body.getAngle();
		Vector2 pointingDirection = new Vector2(MathUtils.cos(bodyAngle), -MathUtils.sin(bodyAngle));
		float flyingAngle = MathUtils.atan2(body.getLinearVelocity().y, body.getLinearVelocity().x);

		Vector2 flightDirection = new Vector2(MathUtils.cos(flyingAngle), MathUtils.sin(flyingAngle));
		float dot = flightDirection.dot(pointingDirection);
		float dragForceMagnitude = (1 - Math.abs(dot)) * flightSpeed * flightSpeed * dragConstant * body.getMass();
		Vector2 arrowTailPosition = body.getWorldPoint(new Vector2(-.3f, 0));
		body.applyForce(new Vector2((dragForceMagnitude * -flightDirection.x), (dragForceMagnitude * -flightDirection.y)), arrowTailPosition, true);
	}

	@Override
	public void draw(SpriteBatch batch, SkeletonRenderer skeletonRenderer, float delta) {
		
	}

	@Override
	public void contact(Entity entity, Contact contact) {
		
	}

	@Override
	public String getName() {
		return name;
	}

}
