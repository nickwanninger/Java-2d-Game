package io.nickw.game.weapon;

public interface Weapon {
	int getDamage();
	int getCooldownTime();
	int getUseCount();
	int getCost();
	String getName();
	String getDescription();
	float getSpeed();
}
