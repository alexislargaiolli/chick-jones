package fr.alex.games.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Fly extends Property{

	Vector2 counterForce;
	
	public Fly(UserData owner) {
		super(owner);
		counterForce = new Vector2(0, 4f);
	}

	@Override
	public void update(float delta) {
		if(!owner.isDead()){
			if(getBody().getLinearVelocity().y < 0){
				getBody().applyForceToCenter(counterForce, true);
			}
		}
	}

	@Override
	public void draw(SpriteBatch batch) {
		
	}	
	
}
