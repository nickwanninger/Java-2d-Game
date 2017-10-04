package io.nickw.game.gfx;

import io.nickw.game.Coordinate;

public class SpriteReference {
	// position of the sprite on the sheet
	public Coordinate pos = new Coordinate(0,0);
	// width & height of the sprite on the sheet
	public int width = 8;
	public int height = 8;

	public SpriteReference(int x, int y, int w, int h) {
		pos.x = x;
		pos.y = y;
		width = w;
		height = h;


	}
	public SpriteReference(Coordinate t, int w, int h) {
		this(t.x, t.y, w, h);
	}

	public SpriteReference(int x, int y) {
		this(x,y,8,8);
	}

}
