package io.nickw.game.weapon;

import io.nickw.game.util.Ownership;

public class ProjectileData {
	public double chaos = 0.0;
	public Ownership team = Ownership.Foe;
	public int damage = 0;

	public ProjectileData(int dmg, Ownership t) {
		team = t;
		damage = dmg;

	}

}