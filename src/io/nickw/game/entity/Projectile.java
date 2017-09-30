package io.nickw.game.entity;

import io.nickw.game.Bounds;
import io.nickw.game.Coordinate;
import io.nickw.game.Vector2;
import io.nickw.game.gfx.LightingType;
import io.nickw.game.gfx.SpriteReference;
import io.nickw.game.level.Level;

public class Projectile extends Entity {

	public Projectile(float x, float y, Vector2 v, Level l) {
		super(x, y, l);
		this.lightingType = LightingType.Fancy;
		this.level = l;
		this.velocity = v;
		this.sprite = new SpriteReference(new Coordinate(0, 88), 8, 8);
		this.bounds = new Bounds(3, 3, 3, 3);
	}

	public int getLightRadius() {
		return 50;
	}

	public void tick() {
		move();
	}

	@Override
	public void hitWall() {
		destroy();
	}
}