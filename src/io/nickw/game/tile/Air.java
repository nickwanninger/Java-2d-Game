package io.nickw.game.tile;

import io.nickw.game.Coordinate;
import io.nickw.game.gfx.Screen;
import io.nickw.game.gfx.SpriteReference;
import io.nickw.game.level.Level;

public class Air extends Tile {

	public Air(int id) {
		super(id);
		this.passable = true;
		sprite = new SpriteReference(new Coordinate(0, 0), 8, 8);
	}

	public void render(Screen screen, Level level, int x, int y) {
		return;
	}

}
