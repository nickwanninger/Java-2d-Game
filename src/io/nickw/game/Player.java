package io.nickw.game;

import java.util.Random;

import io.nickw.game.gfx.Animation;
import io.nickw.game.gfx.Screen;
import io.nickw.game.gfx.SpriteReference;
import io.nickw.game.level.Level;
import io.nickw.game.sound.Sound;
import io.nickw.game.tile.Tile;

public class Player extends GameObject {

	Random rng = new Random(100);
	Vector2 velocity = new Vector2(0, 0);

	private InputHandler input;
	boolean moving = false;
	boolean walking = false;
	int flipBits = 0x00;
	Level level;
	int attackCooldown = 200;
	long lastAttacked = System.currentTimeMillis();
	SpriteReference idleSprite = new SpriteReference(new Coordinate(0, 56), 8, 8);
	Animation walkAnimation = new Animation(4, new Coordinate(8, 56));
	BoundingBox bb = new BoundingBox(new Coordinate(0,0),0,0);

	public int tickCount = 0;

	public int maxHealth = 8;
	public int health = 8;

	// up = 0;
	// right = 1;
	// down = 2;
	// left = 3;
	int direction = 0;

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
			velocity.y -= 1.6;
		if (input.attack.down) {
			long now = System.currentTimeMillis();
			if (now - attackCooldown >= lastAttacked) {
				lastAttacked = now;
				attack();
			}
		}

		velocity.y += 0.2;
		
//		velocity.y = Math.min(velocity.y, 1);

		// clamps the X position to the level's width and 0;
		if (this.position.x + velocity.x + 8 > level.width * 8)
			velocity.x = 0;
		if (this.position.x + velocity.x + 1 <= 0)
			velocity.x = 0;
		// clamps the Y position to the level's height and 0;
		if (this.position.y + velocity.y + 8 > level.height * 8)
			velocity.y = 0;
		if (this.position.y + velocity.y + 1 <= 0)
			velocity.y = 0;

		// gets the total unsigned movement
		int movementMagnitude = (int) Math.abs(Math.sqrt((Math.pow(velocity.x, 2) + Math.pow(velocity.y, 2))));
		// if the unsigned movement magnitude is greater than 0, we must be moving.
		moving = movementMagnitude > 0 ? true : false;
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
		this.position.x += velocity.x;
		this.position.y += velocity.y;
		
		bb = new BoundingBox(new Coordinate(this.position.x + 2, this.position.y + 1), 4, 7);
		
		
		updateCollisions();	
		
		velocity.x = 0;
	}

	
	private void updateCollisions() {
		Coordinate[] tileIntersections = bb.getTileIntersections(level);
		for (int i = 0; i < tileIntersections.length; i++) {
			Tile t = level.getTile(tileIntersections[i].x, tileIntersections[i].y);
			if (t == null) continue;
			if (t.passable) continue;
		}
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
