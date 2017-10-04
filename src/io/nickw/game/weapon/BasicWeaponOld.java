package io.nickw.game.weapon;

import io.nickw.game.entity.projectile.Projectile;

import java.util.Timer;

public class BasicWeaponOld implements Weapon {

	protected int damage = 1;
	protected int cooldownTime = 5;
	protected int cost = 5;
	@Override
	public int getDamage() {
		return damage;
	}

	@Override
	public int getCooldownTime() {
		return cooldownTime;
	}

	@Override
	public int getUseCount() {
		return -1;
	}

	@Override
	public int getCost() {
		return cost;
	}

	@Override
	public String getName() {
		return "Basic Weapon";
	}

	@Override
	public String getDescription() {
		return "As Basic as it gets.";
	}

	@Override
	public float getSpeed() {
		return 3f;
	}
}