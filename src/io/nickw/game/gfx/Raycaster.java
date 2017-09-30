package io.nickw.game.gfx;

import io.nickw.game.Coordinate;
import io.nickw.game.Vector2;
import io.nickw.game.level.Level;
import io.nickw.game.tile.Tile;

public class Raycaster {
	public static Coordinate run(Coordinate start, Coordinate end, Level level, int deadZone) {
		double dx = end.x - start.x;
		double dy = end.y - start.y;
		double stepCount = Math.max(Math.abs(dx), Math.abs(dy));
		double stepX = dx / stepCount;
		double stepY = dy / stepCount;
		double x = start.x;
		double y = start.y;
		for (int i = 0; i < stepCount + 1; i++) {
			int px = (int) Math.round(x);
			int py = (int) Math.round(y);
			int tx = px / Tile.TILE_WIDTH;
			int ty = py / Tile.TILE_WIDTH;
			if (i > deadZone && !level.getTile(tx, ty).passable) {
				return new Coordinate(px, py);
			}
			x = x + stepX;
			y = y + stepY;
		}
		return end;
	}
}