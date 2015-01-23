package fr.alex.games.box2d.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Contact;
import com.esotericsoftware.spine.SkeletonRenderer;

public class NoChickenCollision extends Component{

	public NoChickenCollision(Entity entity) {
		super(entity);
		
	}

	@Override
	public void update(float delta) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void draw(SpriteBatch batch, SkeletonRenderer skeletonRenderer, float delta) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void contact(Entity entity, Contact contact) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

}
