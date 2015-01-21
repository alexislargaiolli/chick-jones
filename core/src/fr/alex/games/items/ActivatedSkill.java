package fr.alex.games.items;

public class ActivatedSkill {
	private ActiveSkill skill;
	private float elapsedTime;
	
	public ActivatedSkill(ActiveSkill skill) {
		super();
		this.skill = skill;
		this.elapsedTime = 0f;
	}
	
	public void update(float delta){
		elapsedTime += delta;
	}
	
	public boolean isOver(){
		return elapsedTime > skill.getDuration();
	}
	
	public boolean isReloaded(){
		return elapsedTime - skill.getDuration() > skill.getCooldown();
	}

	public ActiveSkill getSkill() {
		return skill;
	}

	public void setSkill(ActiveSkill skill) {
		this.skill = skill;
	}
	
	public int getCounterValue(){
		return Math.round(skill.getCooldown() - (elapsedTime - skill.getDuration()));
	}

	public float getElapsedTime() {
		return elapsedTime;
	}

	public void setElapsedTime(float elapsedTime) {
		this.elapsedTime = elapsedTime;
	}

}
