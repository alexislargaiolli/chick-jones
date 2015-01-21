package fr.alex.games.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Move extends Property {

	protected Vector2 origin;
	protected float delta;
	protected float speed;
	protected boolean mooveX;
	protected boolean mooveY;

	public Move(UserData owner, Vector2 origin, float delta, float speed, boolean mooveX, boolean mooveY) {
		super(owner);
		this.origin = origin;
		this.delta = delta;
		this.speed = speed;
		this.mooveX = mooveX;
		this.mooveY = mooveY;
	}

	@Override
	public void update(float delta) {
		if (!owner.isDead()) {
			if (mooveX) {
				getBody().setLinearVelocity(speed, getBody().getLinearVelocity().y);
				if (speed > 0) {
					if (getBody().getPosition().x > (origin.x + this.delta)) {
						speed *= -1;
						owner.getSpatial().flipX();
					}
				} else {
					if (getBody().getPosition().x < (origin.x - this.delta)) {
						speed *= -1;
						owner.getSpatial().flipX();
					}
				}
			}
			if (mooveY) {
				getBody().setLinearVelocity(getBody().getLinearVelocity().x, speed);
				if (speed > 0) {
					if (getBody().getPosition().x > (origin.x + this.delta)) {
						speed *= -1;
					}
				} else {
					if (getBody().getPosition().x < (origin.x - this.delta)) {
						speed *= -1;
					}
				}
			}
		}
	}

	@Override
	public void draw(SpriteBatch batch) {

	}

}
