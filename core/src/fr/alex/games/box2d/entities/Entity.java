package fr.alex.games.box2d.entities;

import java.util.HashMap;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.spine.SkeletonRenderer;

public class Entity {
	private boolean dead;
	private boolean toRemove;
	private Vector2 position;
	private HashMap<String, Component> components;

	public Entity() {
		dead = false;
		toRemove = false;
		position = new Vector2();
		components = new HashMap<String, Component>();
	}

	public void update(float delta) {
		for (Component c : components.values()) {
			c.update(delta);
		}
	}

	public void draw(SpriteBatch batch, SkeletonRenderer skeletonRenderer, float delta) {
		for (Component c : components.values()) {
			c.draw(batch, skeletonRenderer, delta);
		}
	}

	public void contact(Entity entity) {
		for (Component c : components.values()) {
			c.contact(entity);
		}
	}

	public void add(Component component) {
		components.put(component.getName(), component);		
	}

	public Component get(String name) {
		return components.get(name);
	}

	public boolean isDead() {
		return dead;
	}

	public void setDead(boolean dead) {
		this.dead = dead;
	}

	public boolean isToRemove() {
		return toRemove;
	}

	public void setToRemove(boolean toRemove) {
		this.toRemove = toRemove;
	}

	public Vector2 getPosition() {
		return position;
	}

	public void setPosition(Vector2 position) {
		this.position = position;
	}
}
