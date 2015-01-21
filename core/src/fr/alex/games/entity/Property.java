package fr.alex.games.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;

public abstract class Property {
	protected UserData owner;
	
	public Property(UserData owner){
		this.owner = owner;
	}
	
	public abstract void update(float delta);
	
	public abstract void draw(SpriteBatch batch);
	
	protected Body getBody(){
		return owner.getSpatial().getmBody();
	}
	
}
