package fr.alex.games.box2d.entities.components;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.esotericsoftware.spine.SkeletonRenderer;

import fr.alex.games.GM;
import fr.alex.games.box2d.entities.Component;
import fr.alex.games.box2d.entities.ComponentEvent;
import fr.alex.games.box2d.entities.Entity;
import fr.alex.games.box2d.entities.EventType;
import fr.alex.games.box2d.entities.PhysicListener;

public class Coins extends Component implements PhysicListener {
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
	public String getName() {
		return name;
	}

	@Override
	public void beginContact(Entity other, Contact contact) {
		
	}

	@Override
	public void endContact(Entity other, Contact contact) {
		
	}

	@Override
	public void preSolve(Entity other, Contact contact, Manifold oldManifold) {
		if(!collected && other.contains(Collector.name)){
			GM.gold += coinCount;
			contact.setEnabled(false);
			this.entity.broadcastEvent(new ComponentEvent(this, null, EventType.DESTROY));
			collected = true;
		}
	}

	@Override
	public void postSolve(Entity other, Contact contact, ContactImpulse impulse) {
		
	}
	
}
