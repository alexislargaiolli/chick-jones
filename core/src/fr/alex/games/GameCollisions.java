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
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		Object o1 = contact.getFixtureA().getBody().getUserData();
		Object o2 = contact.getFixtureB().getBody().getUserData();

		if (o1 instanceof Entity && o2 instanceof Entity) {
			Entity e1 = (Entity) o1;
			Entity e2 = (Entity) o2;
			e1.contact(e2, contact);
			e2.contact(e1, contact);
		}
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {

	}

}
