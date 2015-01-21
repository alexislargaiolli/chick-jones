package fr.alex.games.items;

import java.util.ArrayList;

public class ActiveSkill {
	private String name;
	private String desc;
	private String icon;
	private float duration;
	private float cooldown;
	private ArrayList<PassiveSkill> passives = new ArrayList<PassiveSkill>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public float getDuration() {
		return duration;
	}

	public void setDuration(float duration) {
		this.duration = duration;
	}

	public float getCooldown() {
		return cooldown;
	}

	public void setCooldown(float cooldown) {
		this.cooldown = cooldown;
	}

	public ArrayList<PassiveSkill> getPassives() {
		return passives;
	}

	public void setPassives(ArrayList<PassiveSkill> passives) {
		this.passives = passives;
	}
}
