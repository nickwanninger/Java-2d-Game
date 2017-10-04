package io.nickw.game.entity;

import io.nickw.game.*;
import io.nickw.game.entity.particle.Smoke;
import io.nickw.game.entity.projectile.Projectile;
import io.nickw.game.gfx.Animation;
import io.nickw.game.gfx.Screen;
import io.nickw.game.gfx.SpriteReference;
import io.nickw.game.item.Inventory;
import io.nickw.game.item.ItemStack;
import io.nickw.game.item.weapon.BasicWeapon;
import io.nickw.game.item.weapon.RingWeapon;
import io.nickw.game.level.Level;
import io.nickw.game.sound.Sounds;
import io.nickw.game.util.Cooldown;
import io.nickw.game.util.InputHandler;
import io.nickw.game.util.Mouse;
import io.nickw.game.util.Timeout;
import io.nickw.game.weapon.BasicWeaponOld;
import io.nickw.game.weapon.Weapon;

public class Player extends Entity {

	public Inventory inventory = new Inventory(this);

	private InputHandler input;
	boolean moving = false;
	boolean walking = false;

	public int health = 100;
	public int maxHealth = 100;
	public int mana = 100;
	public int maxMana = 100;

	private int direction = 0;
	private int tickCount = 0;
	private int flipBits = 0x00;

	public boolean showInventory = false;

	Weapon primaryWeapon = new BasicWeaponOld();

	private Cooldown weaponCooldown;

	Screen screen;

	private long lastUsedMana = System.currentTimeMillis();
	private Animation LRAnimation = new Animation(6, new Coordinate(8, 32), 8, 8);
	private Animation DownAnimation = new Animation(6, new Coordinate(8, 40), 8, 8);
	private Animation UpAnimation = new Animation(6, new Coordinate(8, 48), 8, 8);


	public Player(int x, int y, Level level, InputHandler i) {
		super(x, y, level);
		this.level = level;
		this.bounds = new Bounds(2, 3, 4,5);
		order = 5;
		input = i;
		LRAnimation.frameRate = 15;
		DownAnimation.frameRate = 15;
		UpAnimation.frameRate = 15;
		weaponCooldown = new Cooldown(0);

		inventory.add(new BasicWeapon());
		inventory.add(new RingWeapon());

	}

	public void tick() {
		weaponCooldown.tick();
		inventory.tick();
		tickCount++;

		int timeSinceLastManaUsage = (int) (System.currentTimeMillis() - lastUsedMana);
		if (timeSinceLastManaUsage > 500 && mana <= maxMana) {
			mana += (int) (timeSinceLastManaUsage / 700.0);
		}
		mana = Math.max(0, Math.min(mana, maxMana));

		float moveSpeed = 1f;

		if (input.inventory.clicked) inventory.toggle();

		if (!inventory.visible) {
			// apply the input to the speed value.
			if (input.right.down) velocity.x = moveSpeed;

			if (input.left.down) velocity.x = -moveSpeed;

			if (input.up.down) velocity.y = -moveSpeed;

			if (input.down.down) velocity.y = moveSpeed;

			if (Mouse.left.down && inventory.getSelectedItem() != null && inventory.getSelectedItem().canAttack()) {
				if (weaponCooldown.isDone()) {
					attack();
				}
			}
		}


		move();

		// gets the total unsigned movement
		int movementMagnitude = (int) Math.abs(Math.sqrt((Math.pow(velocity.x, 2) + Math.pow(velocity.y, 2))));
		// if the unsigned movement magnitude is greater than 0, we must be moving.
		moving = movementMagnitude > 0;
		if (Math.abs(movementMagnitude) > 0) {
			walking = true;
			SpriteReference prevSprite = LRAnimation.GetCurrentFrame();
			LRAnimation.tick();
			DownAnimation.tick();
			UpAnimation.tick();
			SpriteReference newSprite = LRAnimation.GetCurrentFrame();
			if (prevSprite != newSprite && (LRAnimation.currentFrame % 2 == 0)) {
				float sx = (int) (position.x + 3 - Math.signum(velocity.x));
				float sy = position.y + 6 - (int) (Math.random() * 4);
				Smoke s = new Smoke(sx, sy, level);
				s.velocity = velocity;
				level.addObject(s);
			}
			if (Math.abs(velocity.x) > 0) {
				if (velocity.x < 0) {
					flipBits = 0x01;
					direction = 3;
				} else {
					flipBits = 0x00;
				}
			}
		} else {
			walking = false;
		}
		velocity.x = 0;
		velocity.y = 0;
	}

	public boolean useMana(int m) {
		if (mana >= m) {
			lastUsedMana = System.currentTimeMillis();
			mana -= m;
			return true;
		} else {
			return false;
		}
	}

	public void attack() {
		ItemStack weapon = inventory.getSelectedItem();
		weaponCooldown.reset(weapon.getCooldownTime());
		int manaCost = weapon.getManaCost();
		double shotCount = weapon.getShotCount();
		double speed = weapon.getProjectileSpeed();
		if (mana >= manaCost * shotCount) {

			double halfCount = (shotCount - 1) / 2;
			double aimAngle = getAttackAngle();
			double shotAngle = weapon.getShotAngle();
			int fireCount = 0;
			for (double i = -halfCount; i <= halfCount; i++) {

				double angle = aimAngle + shotAngle * i;
				new Timeout(() -> {
					useMana(manaCost);
					Sounds.shoot.play(0.05f);
					Vector2 v = getAttackVector(speed, angle);
					Projectile p = new Projectile(position.x, position.y, v, level);
					level.addObject(p);
				}, fireCount * weapon.getShotDelay());

				fireCount++;
			}
		} else {
			Sounds.error.play(0.5f);
		}
	}
	public Vector2 getAttackVector(double speed) {
		return getAttackVector(speed, getAttackAngle());
	}
	public Vector2 getAttackVector(double speed, double angle) {
		float x = (float) (speed * Math.cos(angle));
		float y = (float) (speed * Math.sin(angle));
		return new Vector2(x, y);
	}

	public double getAttackAngle() {
		float x = (screen.offset.x + Game.mouseX) - (position.x + 4);
		float y = (screen.offset.y + Game.mouseY) - (position.y + 4);
		return Math.atan2(y, x);
	}

	public void render(Screen screen) {
		this.screen = screen;
		// get the sprite to draw
		SpriteReference dS = new SpriteReference(new Coordinate(0, 32), 8, 8);;
		if (dir == Direction.EAST || dir == Direction.WEST) {

			if (walking) {
				dS = LRAnimation.GetCurrentFrame();
			}
		}

		if (dir == Direction.SOUTH) {

			if (walking) {
				dS = DownAnimation.GetCurrentFrame();
			} else {
				dS = new SpriteReference(new Coordinate(0, 40), 8, 8);
			}
		}

		if (dir == Direction.NORTH) {
			if (walking) {
				dS = UpAnimation.GetCurrentFrame();
			} else {
				dS = new SpriteReference(new Coordinate(0, 48), 8, 8);
			}
		}

		drawShadow(screen);

		screen.drawSprite(dS, Math.round(position.x), Math.round(position.y), flipBits);
	}

	public void drawMenus(Screen screen) {
		inventory.render(screen);
	}


	public int getLightRadius() {
		int d = (int) (Math.random() * 10) ;
		return 30 + d;
	}
	public float getLightIntensity() {
		return 1f;
	}

}
