package io.nickw.game.tile;

import io.nickw.game.Coordinate;
import io.nickw.game.gfx.Screen;
import io.nickw.game.gfx.SpriteReference;
import io.nickw.game.level.Level;

import java.util.Random;

public class Floor extends Tile {

	public Floor(int id) {
		super(id);
		passable = true;
		sprite = new SpriteReference(new Coordinate(8, 0), 8, 8);
	}

	public void render(Screen screen, Level level, int x, int y) {
		int tx = x / Tile.TILE_WIDTH;
		int ty = y / Tile.TILE_WIDTH;
		int seed = x % 19 * y + x;
		int tOffset = new Random(seed).nextInt(6) * 8;
		if (level.getTileType(tx, ty - 1) != id) {
			screen.drawSprite(new SpriteReference(new Coordinate(tOffset, 16), 8, 8), x, y);
		} else {
			screen.drawSprite(new SpriteReference(new Coordinate(tOffset, 24), 8, 8), x, y,  (x * y % 2)+ 1);
		}
	}

}