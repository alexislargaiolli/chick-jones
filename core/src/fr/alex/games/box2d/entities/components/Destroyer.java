package fr.alex.games.box2d.entities.components;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Contact;
import com.esotericsoftware.spine.SkeletonRenderer;

import fr.alex.games.box2d.entities.Component;
import fr.alex.games.box2d.entities.Entity;

public class Destroyer extends Component {
	public final static String name = "destroyer";
	private boolean hasDestroy;

	public Destroyer(Entity entity) {
		super(entity);
		hasDestroy = false;
	}

	@Override
	public void update(float delta) {

	}

	@Override
	public void draw(SpriteBatch batch, SkeletonRenderer skeletonRenderer, float delta) {

	}

	@Override
	public void contact(Entity entity, Contact contact) {
		if (entity.contains(Destroyable.name)) {
			hasDestroy = true;
			contact.setEnabled(false);			
		}
	}

	@Override
	public String getName() {
		return name;
	}

	public boolean hasDestroy() {
		return hasDestroy;
	}

}
