package fr.alex.games.box2d.entities.components;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Contact;
import com.esotericsoftware.spine.SkeletonRenderer;

import fr.alex.games.box2d.entities.Component;
import fr.alex.games.box2d.entities.ComponentEvent;
import fr.alex.games.box2d.entities.Entity;
import fr.alex.games.box2d.entities.EventType;

public class Stickable extends Component {
	public final static String name = "stickable";
	private int maxStick;

	public Stickable(Entity entity, int maxStick) {
		super(entity);
		this.maxStick = maxStick;
		entity.addListner(this);
	}

	@Override
	public void update(float delta) {

	}

	@Override
	public void draw(SpriteBatch batch, SkeletonRenderer skeletonRenderer, float delta) {

	}

	@Override
	public void onEvent(ComponentEvent event) {
		if (event.getType() == EventType.ARROW_STICK) {
			maxStick--;
			if (maxStick == 0) {
				this.entity.broadcastEvent(new ComponentEvent(this, null, EventType.DESTROY));
			}
		}
	}

	@Override
	public void contact(Entity other, Contact contact) {

	}

	@Override
	public String getName() {
		return name;
	}

}
