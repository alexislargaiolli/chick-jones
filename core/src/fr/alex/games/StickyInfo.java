package fr.alex.games;

import com.badlogic.gdx.math.Vector2;

import fr.alex.games.entity.Arrow;
import fr.alex.games.entity.UserData;

public class StickyInfo {
	private Arrow arrow;
	private UserData userData;
	private Vector2 anchor;

	public StickyInfo(Arrow arrow, UserData userData, Vector2 anchor) {
		super();
		this.arrow = arrow;
		this.userData = userData;
		this.anchor = anchor;
	}

	public Arrow getArrow() {
		return arrow;
	}

	public void setArrow(Arrow arrow) {
		this.arrow = arrow;
	}

	public UserData getUserData() {
		return userData;
	}

	public void setUserData(UserData userData) {
		this.userData = userData;
	}

	public Vector2 getAnchor() {
		return anchor;
	}

	public void setAnchor(Vector2 anchor) {
		this.anchor = anchor;
	}

}
