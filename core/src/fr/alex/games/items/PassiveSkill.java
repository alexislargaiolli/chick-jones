package fr.alex.games.items;

public class PassiveSkill {
	private float bonus;
	private Caracteristic carac;
	private ModifType type;

	public float getBonus() {
		return bonus;
	}

	public void setBonus(float bonus) {
		this.bonus = bonus;
	}

	public Caracteristic getCarac() {
		return carac;
	}

	public void setCarac(Caracteristic carac) {
		this.carac = carac;
	}

	public ModifType getType() {
		return type;
	}

	public void setType(ModifType type) {
		this.type = type;
	}

}
