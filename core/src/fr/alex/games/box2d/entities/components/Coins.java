package fr.alex.games.box2d.entities.components;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Contact;
import com.esotericsoftware.spine.SkeletonRenderer;

import fr.alex.games.GM;
import fr.alex.games.box2d.entities.Component;
import fr.alex.games.box2d.entities.ComponentEvent;
import fr.alex.games.box2d.entities.Entity;
import fr.alex.games.box2d.entities.EventType;

public class Coins extends Component{
	public final static String name = "coin";
	private int coinCount;
	private boolean collected;

	public Coins(Entity entity, int coinCount) {
		super(entity);
		this.coinCount = coinCount;
	}

	@Override
	public void update(float delta) {
		
	}

	@Override
	public void draw(SpriteBatch batch, SkeletonRenderer skeletonRenderer, float delta) {
		
	}

	@Override
	public void contact(Entity other, Contact contact) {
		if(!collected && other.contains(Collector.name)){
			GM.gold += coinCount;
			contact.setEnabled(false);
			this.entity.broadcastEvent(new ComponentEvent(this, null, EventType.DESTROY));
			collected = true;
		}
	}

	@Override
	public String getName() {
		return name;
	}
	
}
