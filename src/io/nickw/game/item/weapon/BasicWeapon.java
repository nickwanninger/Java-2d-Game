package io.nickw.game.item.weapon;

import io.nickw.game.gfx.SpriteReference;
import io.nickw.game.item.ItemStack;

public class BasicWeapon extends ItemStack {
	public BasicWeapon() {
		name = "Staff";
		uiSprite = new SpriteReference(0, 176);
		worldSprite = new SpriteReference(0, 176);
		damage = 10;
		cooldownTime = 10;

		shotCount = 1;
		shotAngle = Math.PI / 15d;

		shotDelay = 10;
		projectileSpeed = 3f;
		manaCost = 10;
	}
	public boolean canAttack() {
		return true;
	}


}