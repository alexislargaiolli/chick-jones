package fr.alex.games;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public class StickyInfo {
	private Body b1;
	private Body b2;
	private Vector2 anchor;

	public StickyInfo(Body b1, Body b2, Vector2 anchor) {
		super();
		this.b1 = b1;
		this.b2 = b2;
		this.anchor = anchor;
	}

	public Vector2 getAnchor() {
		return anchor;
	}

	public void setAnchor(Vector2 anchor) {
		this.anchor = anchor;
	}

	public Body getB1() {
		return b1;
	}

	public void setB1(Body b1) {
		this.b1 = b1;
	}

	public Body getB2() {
		return b2;
	}

	public void setB2(Body b2) {
		this.b2 = b2;
	}
}
