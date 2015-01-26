package fr.alex.games.box2d.entities.components;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Contact;
import com.esotericsoftware.spine.SkeletonRenderer;

import fr.alex.games.box2d.entities.Component;
import fr.alex.games.box2d.entities.Entity;

public class Mortal extends Component{

	public final static String name = "mortal";
	
	public Mortal(Entity entity) {
		super(entity);
		
	}

	@Override
	public void update(float delta) {
		
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
