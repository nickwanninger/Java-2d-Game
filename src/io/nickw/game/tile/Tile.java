package io.nickw.game.tile;

import io.nickw.game.Coordinate;
import io.nickw.game.gfx.Screen;
import io.nickw.game.gfx.SpriteReference;
import io.nickw.game.level.Level;

@SuppressWarnings("StaticInitializerReferencesSubClass")
public class Tile {

	public boolean passable = false;
	public static final int TILE_WIDTH = 8;
	public byte id;
	public SpriteReference sprite = new SpriteReference(new Coordinate(0, 0), 8, 8);
	// create an array of the tiles
	public static Tile[] tiles = new Tile[256];
	public static Tile air = new Air(0);
	public static Tile floor = new Floor(1);
	public static Tile wall = new Wall(2);


	public Tile(int id) {
		this.id = (byte) id;
		if (tiles[id] != null) throw new RuntimeException("Duplicate tile ids!");
		tiles[id] = this;
	}

	public void render(Screen screen, Level level, int x, int y) {
		screen.drawSprite(sprite, x, y);
	}

}
