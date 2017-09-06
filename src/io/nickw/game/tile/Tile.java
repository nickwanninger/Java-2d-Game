package io.nickw.game.tile;

import io.nickw.game.Coordinate;
import io.nickw.game.GameObject;
import io.nickw.game.gfx.SpriteReference;
import io.nickw.game.level.Level;

public class Tile extends GameObject {

	public boolean passable = false;
	public int width = 8;
	public int height = 8;
	public Tile(int x, int y, Level l) {
		super(x, y, l);
		sprite = new SpriteReference(new Coordinate(16, 0), 8, 8);
	}
	
}
