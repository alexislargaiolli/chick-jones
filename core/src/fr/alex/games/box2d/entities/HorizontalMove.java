package fr.alex.games.box2d.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.esotericsoftware.spine.SkeletonRenderer;

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
		/*if (speed > 0) {
			if (entity.getPosition().x > (origin.x + this.delta)) {
				speed *= -1;
				//flip();
			}
		} else {
			if (entity.getPosition().x < (origin.x - this.delta)) {
				speed *= -1;
				//flip();
			}
		}*/
		float x = entity.getPosition().x + speed;
		float y = entity.getPosition().y;
		entity.setPosition(x, y);
	}

	private void flip(){
		Component drawable = entity.get(SpriteComponent.name);
		if(drawable != null){
			((SpriteComponent) drawable).flipX();
		}
		drawable = entity.get(SkeletonComponent.name);
		if(drawable != null){
			((SkeletonComponent) drawable).flipX();
		}
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
