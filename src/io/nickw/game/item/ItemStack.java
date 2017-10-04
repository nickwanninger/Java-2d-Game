package io.nickw.game.item;

import io.nickw.game.entity.Entity;
import io.nickw.game.entity.Player;
import io.nickw.game.gfx.Screen;
import io.nickw.game.gfx.SpriteReference;
import io.nickw.game.level.Level;

public class ItemStack {

	public int count = 1;
	protected SpriteReference uiSprite;
	protected SpriteReference worldSprite;
	protected int id = 0;
	protected String name = "Default Item";

	protected int damage;
	protected int cooldownTime;
	protected double shotAngle;
	protected int shotCount;
	protected int shotDelay;
	protected float projectileSpeed;
	protected int manaCost;

	public void drawInventory(Screen screen, int x, int y) {
		screen.drawSpriteGUI(uiSprite, x, y);
	}

	public void drawSprite(Screen screen, int x, int y) {
		screen.drawSprite(worldSprite, x, y);
	}

	public String getName() {
		return name;
	}

	public boolean canAttack() {
		return false;
	}

	public boolean isBroken() {
		return false;
	}

	public int getAttackDamage() {
		return 0;
	}

	public boolean useOn(Entity e, Player p, Level level, int xt, int yt) {
		return false;
	}

	public boolean matches(ItemStack item) {
		return item.getClass() == getClass();
	}


	public int getDamage() {
		return damage;
	}
	public int getCooldownTime() {
		return cooldownTime;
	}
	public double getShotAngle() {
		return shotAngle;
	}
	public int getShotCount() {
		return shotCount;
	}
	public int getShotDelay() {
		return shotDelay;
	}
	public float getProjectileSpeed() {
		return projectileSpeed;
	}

	public int getManaCost() {
		return manaCost;
	}


}