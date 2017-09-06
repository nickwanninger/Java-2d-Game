package io.nickw.game.entity;

import io.nickw.game.Coordinate;
import io.nickw.game.GameObject;
import io.nickw.game.Vector2;
import io.nickw.game.level.Level;
import io.nickw.game.*;
import io.nickw.game.gfx.SpriteReference;
import io.nickw.game.tile.Tile;
import java.util.Random;

public class Entity extends GameObject {

	public Vector2 velocity = new Vector2(0, 0);
	public Level level;
	public boolean grounded = false;
	public BoundingBox bb = new BoundingBox(new Coordinate(0,0),0,0);

	// constructor
	public Entity(int x, int y, Level l) {
		// construct super class
		super(x, y, l);
	}

	public void move() {
		velocity.y = Math.min(7f, velocity.y);
		this.position.x += velocity.x;
		this.position.y += velocity.y;
		bb = new BoundingBox(new Coordinate(this.position.x + 2, this.position.y + 1), 4, 7);
		handleCollisions(level);
	}

	// will adjust the velocity and positions so that the player can collide with a level.tiles where passable = false.
	public void handleCollisions(Level level) {
		Tile[] tiles = bb.getTileIntersections(level);
		grounded = false; // reset the "grounded" variable to false, basically always assume the player can jump
	}

}