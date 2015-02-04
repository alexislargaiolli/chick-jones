package fr.alex.games;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;

import fr.alex.games.box2d.entities.Entity;
import fr.alex.games.entity.Chicken;

public class GameCollisions implements ContactListener {

	@Override
	public void beginContact(Contact contact) {
		Object fixtureUserData = contact.getFixtureA().getUserData();

		if (fixtureUserData instanceof Chicken) {
			((Chicken) fixtureUserData).incrementFootContact();
		}

		fixtureUserData = contact.getFixtureB().getUserData();
		if (fixtureUserData instanceof Chicken) {
			((Chicken) fixtureUserData).incrementFootContact();
		}
		
		Object o1 = contact.getFixtureA().getBody().getUserData();
		Object o2 = contact.getFixtureB().getBody().getUserData();

		if (o1 instanceof Entity && o2 instanceof Entity) {
			Entity e1 = (Entity) o1;
			Entity e2 = (Entity) o2;
			e1.beginContact(e2, contact);
			e2.beginContact(e1, contact);
		}
	}

	@Override
	public void endContact(Contact contact) {
		Object fixtureUserData = contact.getFixtureA().getUserData();

		if (fixtureUserData instanceof Chicken) {
			((Chicken) fixtureUserData).decrementFootContact();
		}

		fixtureUserData = contact.getFixtureB().getUserData();
		if (fixtureUserData instanceof Chicken) {
			((Chicken) fixtureUserData).decrementFootContact();
		}
		
		Object o1 = contact.getFixtureA().getBody().getUserData();
		Object o2 = contact.getFixtureB().getBody().getUserData();

		if (o1 instanceof Entity && o2 instanceof Entity) {
			Entity e1 = (Entity) o1;
			Entity e2 = (Entity) o2;
			e1.endContact(e2, contact);
			e2.endContact(e1, contact);
		}
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		Object o1 = contact.getFixtureA().getBody().getUserData();
		Object o2 = contact.getFixtureB().getBody().getUserData();

		if (o1 instanceof Entity && o2 instanceof Entity) {
			Entity e1 = (Entity) o1;
			Entity e2 = (Entity) o2;
			e1.preSolve(e2, contact, oldManifold);
			e2.preSolve(e1, contact, oldManifold);
		}
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		Object o1 = contact.getFixtureA().getBody().getUserData();
		Object o2 = contact.getFixtureB().getBody().getUserData();

		if (o1 instanceof Entity && o2 instanceof Entity) {
			Entity e1 = (Entity) o1;
			Entity e2 = (Entity) o2;
			e1.postSolve(e2, contact, impulse);
			e2.postSolve(e1, contact, impulse);
		}
	}

}
