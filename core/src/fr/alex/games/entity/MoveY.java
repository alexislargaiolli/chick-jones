package fr.alex.games.entity;

import com.badlogic.gdx.math.Vector2;

public class MoveY extends Move{

	public MoveY(UserData owner, Vector2 origin, float delta, float speed) {
		super(owner, origin, delta, speed, true, false);		
	}

}
