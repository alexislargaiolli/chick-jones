package fr.alex.games.box2d.entities.components;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.esotericsoftware.spine.SkeletonRenderer;

import fr.alex.games.box2d.entities.Component;
import fr.alex.games.box2d.entities.Entity;

public class HorizontalMove extends Component{
	public final static String name = "hmove";
	
	protected Vector2 origin;
	protected float delta;
	protected float speed;
	
	public HorizontalMove(Entity entity, float delta, float speed) {
		super(entity);
		origin = entity.getPosition().cpy();
		this.delta = delta;
		this.speed = speed;
	}

	@Override
	public void update(float delta) {
		float x = entity.getPosition().x + speed;
		float y = entity.getPosition().y;
		if(entity.contains(Box2dSkeletonBasic.name)){
			Box2dSkeletonBasic skel = (Box2dSkeletonBasic) entity.get(Box2dSkeletonBasic.name);
			skel.getBody().setLinearVelocity(speed, skel.getBody().getLinearVelocity().y);
		}
		entity.setPosition(x, y);
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
