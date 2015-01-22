package fr.alex.games.box2d.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.esotericsoftware.spine.SkeletonRenderer;

public abstract class Component implements Comparable<Component> {
	protected String name;
	protected int drawOrder;

	public Component(String name) {
		this.name = name;
		drawOrder = 0;
	}

	public abstract void update(float delta);

	public abstract void draw(SpriteBatch batch, SkeletonRenderer skeletonRenderer, float delta);

	public abstract void contact(Entity entity);

	@Override
	public int compareTo(Component o) {
		if (drawOrder > o.getDrawOrder()) {
			return 1;
		}
		if (drawOrder < o.getDrawOrder()) {
			return -1;
		}
		return 0;
	}

	public String getName() {
		return name;
	}

	public int getDrawOrder() {
		return drawOrder;
	}
}
