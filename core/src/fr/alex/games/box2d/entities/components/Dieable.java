package fr.alex.games.box2d.entities.components;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Contact;
import com.esotericsoftware.spine.SkeletonRenderer;

import fr.alex.games.box2d.entities.Component;
import fr.alex.games.box2d.entities.Entity;

public class Dieable extends Component{
	public final static String name = "dieable";

	public Dieable(Entity entity) {
		super(entity);
		
	}

	@Override
	public void update(float delta) {
		
	}

	@Override
	public void draw(SpriteBatch batch, SkeletonRenderer skeletonRenderer, float delta) {
		
	}

	@Override
	public void contact(Entity other, Contact contact) {
		if(other.contains(Mortal.name)){
			this.entity.setDead(true);
		}
	}

	@Override
	public String getName() {
		return name;
	}

}
