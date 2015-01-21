package fr.alex.games.entity;

import com.badlogic.gdx.physics.box2d.Body;

import fr.alex.games.GM;

public class UserData {
	int life = 1;
	int coins = 0;
	boolean alreadyHit;
	boolean destroyable;
	boolean mortal;
	boolean coin;
	boolean star;
	boolean stick;
	boolean remove;
	float timeBeforeRemove = 0;
	SimpleSpatial spatial;

	public UserData(SimpleSpatial spatial) {
		this.spatial = spatial;
	}

	public boolean isDead() {
		return life == 0;
	}

	public void hit(Body body) {
		if (coin && !alreadyHit) {
			GM.gold += coins;
			coins = 0;
			EffectManager.get().goldEffect(body.getPosition().x, body.getPosition().y);
		}
		if (star && !alreadyHit) {
			GM.stars += 1;
		}
		if (destroyable && !isDead()) {
			life--;
			spatial.nextImage();
			if(timeBeforeRemove == 0){
				remove = true;
			}
			if (isDead()) {
				EffectManager.get().dustEffect(body.getPosition().x, body.getPosition().y);
			}
		}
		alreadyHit = true;
	}

	public void update(float delta) {
		if (destroyable && isDead() && !remove) {
			timeBeforeRemove -= delta;
			if(timeBeforeRemove <= 0){
				remove = true;
			}
		}
	}

	public void playerContact(Body body, Chicken p) {
		if (mortal) {
			p.die();
		}
		if (coin || star) {
			hit(body);
		}
	}

	public int getLife() {
		return life;
	}

	public void setLife(int life) {
		this.life = life;
	}

	public int getCoins() {
		return coins;
	}

	public void setCoins(int coins) {
		this.coins = coins;
	}

	public boolean isDestroyable() {
		return destroyable;
	}

	public void setDestroyable(boolean destroyable) {
		this.destroyable = destroyable;
	}

	public boolean isMortal() {
		return mortal;
	}

	public void setMortal(boolean mortal) {
		this.mortal = mortal;
	}

	public boolean isCoin() {
		return coin;
	}

	public void setCoin(boolean coin) {
		this.coin = coin;
	}

	public SimpleSpatial getSpatial() {
		return spatial;
	}

	public void setSpatial(SimpleSpatial spatial) {
		this.spatial = spatial;
	}

	public boolean isStick() {
		return stick;
	}

	public void setStick(boolean stick) {
		this.stick = stick;
	}

	public boolean isStar() {
		return star;
	}

	public void setStar(boolean star) {
		this.star = star;
	}

	public boolean isAlreadyHit() {
		return alreadyHit;
	}

	public void setAlreadyHit(boolean alreadyHit) {
		this.alreadyHit = alreadyHit;
	}

	public float getTimeBeforeDie() {
		return timeBeforeRemove;
	}

	public void setTimeBeforeDie(float timeBeforeDie) {
		this.timeBeforeRemove = timeBeforeDie;
	}

	public boolean isRemove() {
		return remove;
	}

	public void setRemove(boolean remove) {
		this.remove = remove;
	}
}