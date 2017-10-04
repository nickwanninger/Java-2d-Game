package io.nickw.game.item.weapon;

import io.nickw.game.gfx.SpriteReference;
import io.nickw.game.item.ItemStack;

public class RingWeapon extends ItemStack {
	public RingWeapon() {
		name = "Ring";
		uiSprite = new SpriteReference(8, 176);
		worldSprite = new SpriteReference(8, 176);
		damage = 10;
		cooldownTime = 25;

		shotCount = 25;
		shotAngle = Math.PI / 12.5d;

		shotDelay = 1;
		projectileSpeed = 1f;
		manaCost = 3;
	}
	public boolean canAttack() {
		return true;
	}


}