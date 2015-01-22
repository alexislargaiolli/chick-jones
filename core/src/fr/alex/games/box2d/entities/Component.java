package fr.alex.games.box2d.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Contact;
import com.esotericsoftware.spine.SkeletonRenderer;

public abstract class Component {
	protected Entity entity;

	public Component(Entity entity) {
		this.entity = entity;
	}

	public abstract void update(float delta);

	public abstract void draw(SpriteBatch batch, SkeletonRenderer skeletonRenderer, float delta);

	public abstract void contact(Entity entity, Contact contact);

	

	public abstract String getName();
}
