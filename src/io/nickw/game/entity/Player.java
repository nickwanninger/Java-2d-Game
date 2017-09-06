package io.nickw.game.entity;

import io.nickw.game.*;
import io.nickw.game.gfx.Animation;
import io.nickw.game.gfx.Screen;
import io.nickw.game.gfx.SpriteReference;
import io.nickw.game.level.Level;
import io.nickw.game.sound.Sound;
import io.nickw.game.tile.Tile;

public class Player extends Entity {


	private InputHandler input;
	boolean moving = false;
	boolean walking = false;

	public int health = 8;
	public int maxHealth = 8;

	private int direction = 0;
	private int tickCount = 0;
	private int flipBits = 0x00;

	private int attackCooldown = 200;
	private long lastAttacked = System.currentTimeMillis();
	private SpriteReference idleSprite = new SpriteReference(new Coordinate(0, 56), 8, 8);
	private Animation walkAnimation = new Animation(4, new Coordinate(8, 56));

	public Player(int x, int y, Level level, InputHandler i) {
		super(x, y, level);
		this.level = level;
		order = 5;
		input = i;
		walkAnimation.frameRate = 15;
	}

	public void tick() {
		tickCount++;
		health = (tickCount / 3) % maxHealth + 1;

		int moveSpeed = 1;
		// apply the input to the speed value.
		if (input.right.down)
			velocity.x += moveSpeed;
		if (input.left.down)
			velocity.x -= moveSpeed;
		if (input.up.down && velocity.y == 0)
			velocity.y -= 12;
		if (input.attack.down) {
			long now = System.currentTimeMillis();
			if (now - attackCooldown >= lastAttacked) {
				lastAttacked = now;
				attack();
			}
		}

		velocity.y += 0.2;

		// gets the total unsigned movement
		int movementMagnitude = (int) Math.abs(Math.sqrt((Math.pow(velocity.x, 2) + Math.pow(velocity.y, 2))));
		// if the unsigned movement magnitude is greater than 0, we must be moving.
		moving = movementMagnitude > 0;
		if (Math.abs(velocity.x) > 0) {
			walking = true;
			walkAnimation.tick();
			if (velocity.x < 0) {
				flipBits = 0x01;
				direction = 3;
			} else {
				flipBits = 0x00;
			}
		} else {
			walking = false;
		}

		move();
		
		velocity.x = 0;
	}

	public void attack() {
		Sound.hit.play();
	}

	public void render(Screen screen) {
		// get the sprite to draw
		SpriteReference dS = idleSprite;
		if (walking) {
			dS = walkAnimation.GetCurrentFrame();
		}
		screen.drawSprite(dS, position.x, position.y, flipBits);
	}

	Coordinate GetAttackSwingPosition() {
		int x = 0;
		int y = 0;
		int r = 4;
		if (direction == 0)
			y = -r;
		if (direction == 1)
			x = r;
		if (direction == 2)
			y = r;
		if (direction == 3)
			x = -r;
		return new Coordinate(position.x + x, position.y + y);
	}
}
