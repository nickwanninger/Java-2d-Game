package io.nickw.game.entity;

import io.nickw.game.*;
import io.nickw.game.gfx.Animation;
import io.nickw.game.gfx.LightingType;
import io.nickw.game.gfx.Screen;
import io.nickw.game.gfx.SpriteReference;
import io.nickw.game.level.Level;
import io.nickw.game.sound.Sound;

public class Player extends Entity {


	private InputHandler input;
	boolean moving = false;
	boolean walking = false;

	public int health = 8;
	public int maxHealth = 8;

	private int direction = 0;
	private int tickCount = 0;
	private int flipBits = 0x00;

	Screen screen;
	private int attackCooldown = 200;
	private long lastAttacked = System.currentTimeMillis();
	private Animation LRAnimation = new Animation(6, new Coordinate(8, 32), 8, 8);
	private Animation DownAnimation = new Animation(6, new Coordinate(8, 40), 8, 8);
	private Animation UpAnimation = new Animation(6, new Coordinate(8, 48), 8, 8);
	public Player(int x, int y, Level level, InputHandler i) {
		super(x, y, level);
		this.level = level;
		this.lightingType = LightingType.Fancy;
		this.bounds = new Bounds(2, 3, 4,5);
		order = 5;
		input = i;
		LRAnimation.frameRate = 15;
		DownAnimation.frameRate = 15;
		UpAnimation.frameRate = 15;
	}

	public void tick() {
		tickCount++;
		health = (tickCount / 3) % maxHealth + 1;

		float moveSpeed = 1f;
		// apply the input to the speed value.
		if (input.right.down) {
			velocity.x = moveSpeed;
		}

		if (input.left.down) {
			velocity.x = -moveSpeed;
		}

		if (input.up.down) {
			velocity.y = -moveSpeed;
		}

		if (input.down.down) {
			velocity.y = moveSpeed;
		}

		if (input.attack.down) {
			long now = System.currentTimeMillis();
			if (now - attackCooldown >= lastAttacked) {
				lastAttacked = now;
				attack();
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
				level.addObject(new Smoke(sx, sy, level));
//				Sound.step.play();

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

	public void attack() {
//		Sound.shoot.play();
		float aimX = screen.offset.x + Game.mouseX - (position.x + 4);
		float aimY = screen.offset.y + Game.mouseY - (position.y + 4);
		double speed = 0.5;
		double multiplier = speed / Math.sqrt(Math.pow(aimX, 2) + Math.pow(aimY, 2));
		aimX *= multiplier;
		aimY *= multiplier;
		Vector2 v = new Vector2(aimX, aimY);
		Projectile p = new Projectile(position.x, position.y, v, level);
		level.addObject(p);
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
//		bounds.render(screen, position);
	}

	public int getLightRadius() {return 50;}

}
