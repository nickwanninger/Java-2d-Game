package io.nickw.game;

import io.nickw.game.gfx.Screen;
import io.nickw.game.level.Level;

public class BoundingBox {
	private Coordinate max, min;
	private int width, height;
	
	BoundingBox(Coordinate t, int w, int h) {
		width = w;
		height = h;
		// minimum and maximum values of the bounding box. (top left and bottom right.)
		max = t;
		min = new Coordinate(t.x + width, t.y + height);
		
	}
	
	public void shift(int x, int y) {
		max = new Coordinate(max.x + x, max.y + y);
		min = new Coordinate(min.x + x, min.y + y);
	}
	
	public void setPosition(Coordinate t) {
		max = t;
		min = new Coordinate(t.x + width, t.y + height);
	}
	
	public void setSize(int w, int h) {
		width = w;
		height = h;
		min = new Coordinate(max.x + width, max.y + height);
	}
	
	
	public void render(Screen screen) {
		int tx = max.x - screen.offset.x;
		int ty = max.y - screen.offset.y;
		 int mx = min.x - screen.offset.x;
		int my = min.y - screen.offset.y;
		screen.drawSquare(tx, ty, mx, my, 0xffffff);
	}

	
	public Coordinate[] getTileIntersections(Level level) {
		Coordinate[] intersections = new Coordinate[4];
		intersections[0] = new Coordinate(max.x / 8, max.y / 8);
		intersections[1] = new Coordinate(max.x / 8, min.y / 8);
		intersections[2] = new Coordinate(min.x / 8, max.y / 8);
		intersections[3] = new Coordinate(min.x / 8, min.y / 8);
		return intersections;
	}
	
	
	public Coordinate getTileOverlap(int x, int y) {
		int rX = x * 8;
		int rY = y * 8;
		return new Coordinate(rX, rY);
	}
}
