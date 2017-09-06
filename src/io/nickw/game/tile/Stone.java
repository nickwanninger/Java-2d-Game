package io.nickw.game.tile;

import io.nickw.game.Coordinate;
import io.nickw.game.gfx.SpriteReference;
import io.nickw.game.level.Level;

public class Stone extends Tile {

	public Stone(int x, int y, Level l) {
		super(x, y, l);
		sprite = new SpriteReference(new Coordinate(16, 0), 8, 8);
		// TODO Auto-generated constructor stub
	}

}
