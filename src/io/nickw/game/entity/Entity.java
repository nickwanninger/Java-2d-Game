package io.nickw.game.entity;

import io.nickw.game.Coordinate;
import io.nickw.game.GameObject;
import io.nickw.game.Vector2;
import io.nickw.game.gfx.Screen;
import io.nickw.game.gfx.SpriteReference;
import io.nickw.game.level.Level;
import io.nickw.game.*;
import io.nickw.game.tile.Tile;

public class Entity extends GameObject {

	public Vector2 velocity = new Vector2(0, 0);
	public Level level;
	public boolean grounded = false;
	Bounds bounds = new Bounds(0,0,8,8);
	Direction dir = Direction.EAST;

	// constructor
	public Entity(int x, int y, Level l) {
		// construct with super class
		super(x, y, l);
	}

	public void move() {
		velocity.y = Math.min(Game.terminalVelocity, velocity.y);
		moveX();
		moveY();
		if (Math.abs(velocity.y) > 0) {
			dir = Math.signum(velocity.y) == 1 ? Direction.SOUTH : Direction.NORTH;
		}
		if (Math.abs(velocity.x) > 0) {
			dir = Math.signum(velocity.x) == 1 ? Direction.EAST : Direction.WEST;
		}
	}


	void moveX() {
		int tx;
		if (velocity.x == 0) return;
		if (velocity.x > 0) { // moving right
			tx = (int) (position.x + bounds.x + bounds.width - 1 + velocity.x) / Tile.TILE_WIDTH;
		} else {
			tx = (int) (position.x + bounds.x + velocity.x) / Tile.TILE_WIDTH;
		}
		boolean topPassable = level.getTile(tx, (position.y + bounds.y) / Tile.TILE_WIDTH).passable;
		boolean bottomPassable = level.getTile(tx, (position.y + bounds.y + bounds.height - 1) / Tile.TILE_WIDTH).passable;
		if (topPassable && bottomPassable) {
			this.position.x += velocity.x;
		} else {
			velocity.x = 0;
		}
	}

	void moveY() {
		if (velocity.y == 0) return;
		int ty;
		// moving up
		if (velocity.y < 0) {
			ty = (int) (position.y + bounds.y + velocity.y) / Tile.TILE_WIDTH;
		} else {
			ty = (int) (position.y + bounds.y + bounds.height - 1 + velocity.y) / Tile.TILE_WIDTH;
		}
		if (level.getTile((position.x + bounds.x + bounds.width - 1) / Tile.TILE_WIDTH, ty).passable && level.getTile((position.x + bounds.x) / Tile.TILE_WIDTH, ty).passable) {
			this.position.y += velocity.y;
		} else {
			velocity.y = 0;
		}

	}

	public void landed() {
		// to be implemented in sub-classes
	}


	public void drawShadow(Screen screen) {
		screen.drawSprite(new SpriteReference(new Coordinate(0,80), 8,8), position.x, position.y);
	}
}