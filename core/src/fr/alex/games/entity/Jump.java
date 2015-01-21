package fr.alex.games.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Jump extends Property{

	Vector2 jump;
	float jumpSpeed;
	float jumpFrequence;
	float elaspedTime;
	
	public Jump(UserData owner, float jumpSpeed, float jumpFrequence) {
		super(owner);
		jump = new Vector2(0, jumpSpeed);
		this.jumpSpeed = jumpSpeed;
		this.jumpFrequence = jumpFrequence;
		this.elaspedTime = 0;
	}

	@Override
	public void update(float delta) {
		if(!owner.isDead()){
			if(elaspedTime >= jumpFrequence){
				getBody().applyLinearImpulse(jump, Vector2.Zero, true);
				elaspedTime = 0;
			}
			elaspedTime += delta;
		}
	}

	@Override
	public void draw(SpriteBatch batch) {
		// TODO Auto-generated method stub
		
	}
	
	
}
