package io.nickw.game.gfx;

import io.nickw.game.Coordinate;

public class SpriteReference {
	// position of the sprite on the sheet
	public Coordinate pos = new Coordinate(0,0);
	// width & height of the sprite on the sheet
	public int width = 8;
	public int height = 8;
	
	public SpriteReference(Coordinate t, int w, int h) {
		pos = t;
		width = w;
		height = h;
	}
}
