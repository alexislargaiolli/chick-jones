package fr.alex.games.box2d.entities;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.Manifold;

public interface PhysicListener {
	public void beginContact(Entity other, Contact contact);

	public void endContact(Entity other, Contact contact);

	public void preSolve(Entity other, Contact contact, Manifold oldManifold);

	public void postSolve(Entity other, Contact contact, ContactImpulse impulse);
}
