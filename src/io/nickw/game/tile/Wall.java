package io.nickw.game.tile;

import io.nickw.game.Coordinate;
import io.nickw.game.gfx.Screen;
import io.nickw.game.gfx.SpriteReference;
import io.nickw.game.level.Level;

import java.util.Random;

public class Wall extends Tile {

	public Wall(int id) {
		super(id);
		sprite = new SpriteReference(new Coordinate(16, 0), 16, 16);
	}

	public void render(Screen screen, Level level, int x, int y) {
		int tx = x / Tile.TILE_WIDTH;
		int ty = y / Tile.TILE_WIDTH;
		int seed = x % 4 * y + x;
		int tOffset = new Random(seed).nextInt(3) * 8;
		if (level.getTileType(tx, ty + 1) == id) {
			screen.drawSprite(new SpriteReference(new Coordinate(tOffset, 0), 8, 8), x, y);
		} else {
			screen.drawSprite(new SpriteReference(new Coordinate(tOffset, 8), 8, 8), x, y);
		}
	}

}
