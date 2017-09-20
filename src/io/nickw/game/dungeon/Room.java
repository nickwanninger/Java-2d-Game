package io.nickw.game.dungeon;

import java.util.Random;

public class Room {
	public int x1 = 0;
	public int y1 = 0;
	public int x2 = 0;
	public int y2 = 0;
	public int width = 0;
	public int height = 0;
	public int color = 0;

	public Room(int x, int y, int w, int h) {
		this.x1 = x;
		this.y1 = y;
		this.x2 = x + w;
		this.y2 = y + h;
		this.width = w;
		this.height = h;
		this.color = new Random().nextInt(0xffffff);
	}

	public void shift (int x, int y) {
		this.x1 += x;
		this.x2 += x;
		this.y1 += y;
		this.y2 += y;
	}

}