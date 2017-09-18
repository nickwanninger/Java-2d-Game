package io.nickw.game.tile;

import io.nickw.game.Coordinate;
import io.nickw.game.GameObject;
import io.nickw.game.gfx.Screen;
import io.nickw.game.gfx.SpriteReference;
import io.nickw.game.level.Level;

public class Tile {

	public boolean passable = false;
	// create an array of the tiles

	public int width = 16;
	public int height = 16;
	public static final int TILE_WIDTH = 16;
	public byte id;
	public SpriteReference sprite = new SpriteReference(new Coordinate(16, 0), 16, 16);

	public static Tile[] tiles = new Tile[256];
	public static Tile air = new Air(0);
	public static Tile stone = new Stone(1);
	public static Tile brick = new Brick(2);

	public Tile(int id) {
		this.id = (byte) id;
		if (tiles[id] != null) throw new RuntimeException("Duplicate tile ids!");
		tiles[id] = this;
	}

	public void render(Screen screen, int x, int y) {
		screen.drawSprite(sprite, x, y);
	}

}
