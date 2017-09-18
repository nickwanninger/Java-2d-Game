package io.nickw.game.tile;

import io.nickw.game.Coordinate;
import io.nickw.game.gfx.SpriteReference;
import io.nickw.game.level.Level;

public class Stone extends Tile {

	public Stone(int id) {
		super(id);
		sprite = new SpriteReference(new Coordinate(16, 0), 16, 16);
	}

}
