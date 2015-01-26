package fr.alex.games.box2d.entities.components;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Contact;
import com.esotericsoftware.spine.SkeletonRenderer;

import fr.alex.games.box2d.entities.Component;
import fr.alex.games.box2d.entities.ComponentEvent;
import fr.alex.games.box2d.entities.Entity;
import fr.alex.games.box2d.entities.EventType;

/**
 * Immediatly destroy a entity when contact an other entity with the component destroyer
 * @author alex
 *
 */
public class Destroyable extends Component {
	public final static String name = "destroyable";
	private boolean trigger;

	public Destroyable(Entity entity) {
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
		if (!trigger && entity.contains(Destroyer.name)) {
			trigger = true;			
			this.entity.broadcastEvent(new ComponentEvent(this, null, EventType.DESTROY));
		}
	}

	@Override
	public String getName() {
		return name;
	}

}
