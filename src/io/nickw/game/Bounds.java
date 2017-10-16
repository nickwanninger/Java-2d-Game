package io.nickw.game;

import io.nickw.game.gfx.Screen;
import io.nickw.game.level.Level;
import io.nickw.game.tile.Tile;

public class Bounds {
	public int x, y;
	public int width, height;
	
	public Bounds(int x, int y, int w, int h) {
		this.x = x;
		this.y = y;
		width = w;
		height = h;
	}

	public void render(Screen screen, Vector2 offset) {
		int tx = x - screen.offset.x + (int) offset.x;
		int ty = y - screen.offset.y + (int) offset.y;
		int mx = x + width - screen.offset.x + (int) offset.x;
		int my = y + height - screen.offset.y + (int) offset.y;
		screen.drawSquare(tx, ty, mx, my, 0xffffff);
	}
}
