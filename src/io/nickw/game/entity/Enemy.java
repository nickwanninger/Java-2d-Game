package io.nickw.game.entity;

import io.nickw.game.Bounds;
import io.nickw.game.gfx.Screen;
import io.nickw.game.level.Level;

public class Enemy extends Entity {
	Bounds bounds = new Bounds(0,0,8,8);
	public Enemy(float x, float y, Level l) {
		super(x, y, l);
	}
}