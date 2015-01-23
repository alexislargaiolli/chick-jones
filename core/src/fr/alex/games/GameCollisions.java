package fr.alex.games;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;

import fr.alex.games.box2d.entities.Entity;
import fr.alex.games.entity.Arrow;
import fr.alex.games.entity.Chicken;
import fr.alex.games.entity.UserData;

public class GameCollisions implements ContactListener {

	public static List<StickyInfo> arrowToStick = new ArrayList<StickyInfo>();

	@Override
	public void beginContact(Contact contact) {
		Object o1 = contact.getFixtureA().getBody().getUserData();
		Object o2 = contact.getFixtureB().getBody().getUserData();
		if (contact.getFixtureA().isSensor()) {
			if (o1 instanceof Chicken) {
				chickenCollideGround((Chicken) o1, o2);
			}
		}
		if (contact.getFixtureB().isSensor()) {
			if (o2 instanceof Chicken) {
				chickenCollideGround((Chicken) o2, o1);
			}
		}
	}

	private void arrowContact(Arrow arrow, Body arrowBody, Object other, Body otherBody, Contact contact) {
		if (other instanceof UserData) {
			UserData ud = (UserData) other;
			ud.hit(otherBody);
			if (arrow.getContactCount() == 0) {
				GM.hitCount++;
			}
			arrow.contact();
			if (ud.isCoin()) {
				contact.setEnabled(false);
			}
			if (ud.isStick()) {
				Vector2 contactPoint = contact.getWorldManifold().getPoints()[0];
				contact.setEnabled(false);
				if (contactPoint.dst(arrow.getHead()) < .3f) {
					//arrowToStick.add(new StickyInfo(arrow, ud, contactPoint));
				} else {
					contact.setEnabled(false);
				}
			}
		} else if (other instanceof Chicken) {
			contact.setEnabled(false);
		}
	}

	private void playerContact(Chicken player, Body playerBody, Object other, Body otherBody, Contact contact) {
		if (other instanceof UserData) {
			UserData ud = ((UserData) other);
			if (ud.isDead()) {
				contact.setEnabled(false);
			} else {
				ud.playerContact(otherBody, player);
			}
		} else if (other instanceof Arrow) {
			contact.setEnabled(false);
			/*
			 * if (((Arrow)
			 * other).getmBody().getLinearVelocity().dst(Vector2.Zero) < 3) {
			 * ((Arrow) other).setDead(true); GM.arrowCount++; }
			 */
		}
	}

	private void chickenCollideGround(Chicken chicken, Object other) {
		if (!(other instanceof UserData)) {
			chicken.onCollideGround();
		}
	}

	@Override
	public void endContact(Contact contact) {
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		Object o1 = contact.getFixtureA().getBody().getUserData();
		Object o2 = contact.getFixtureB().getBody().getUserData();
		Body b1 = contact.getFixtureA().getBody();
		Body b2 = contact.getFixtureB().getBody();
		
		if(o1 instanceof Entity && o2 instanceof Entity){
			Entity e1 = (Entity) o1;
			Entity e2 = (Entity) o2;
			e1.contact(e2, contact);
			e2.contact(e1, contact);			
		}		
		/*if (o1 instanceof Arrow) {
			arrowContact((Arrow) o1, b1, o2, b2, contact);
		} else if (o2 instanceof Arrow) {
			arrowContact((Arrow) o2, b2, o1, b1, contact);
		}
		if (o1 instanceof Chicken) {
			if (contact.getFixtureA().isSensor()) {
			} else {
				playerContact((Chicken) o1, b1, o2, b2, contact);
			}
		} else if (o2 instanceof Chicken) {
			if (contact.getFixtureB().isSensor()) {
			} else {
				playerContact((Chicken) o2, b2, o1, b1, contact);
			}
		}*/
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
	}

}
