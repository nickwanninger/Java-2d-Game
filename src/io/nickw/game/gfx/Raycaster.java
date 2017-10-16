package io.nickw.game.gfx;

import io.nickw.game.Coordinate;
import io.nickw.game.Vector2;
import io.nickw.game.level.Level;
import io.nickw.game.tile.Tile;

public class Raycaster {

	public static class Result {
		public Vector2 position = new Vector2(0,0);
		public boolean hit = false;
		public Result(Vector2 pos, boolean h) {
			position = pos;
			hit = h;
		}
	}

	public static Result run(Coordinate start, Coordinate end, Level l, int d) {
		Vector2 s = start.toVector2();
		Vector2 e = end.toVector2();
		return run(s,e,l,d);
	}

	public static Result run(Vector2 start, Vector2 end, Level level, int deadZone) {
		Result result = new Result(end, false);

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
				result = new Result(new Vector2(px, py), true);
				return result;
			}
			x = x + stepX;
			y = y + stepY;
		}
		return result;
	}
}