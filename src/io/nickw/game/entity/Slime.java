package io.nickw.game.entity;

import io.nickw.game.Bounds;
import io.nickw.game.Coordinate;
import io.nickw.game.gfx.Screen;
import io.nickw.game.gfx.SpriteReference;
import io.nickw.game.level.Level;

import java.util.Random;

public class Slime extends Enemy {
	public Slime(float x, float y, Level l) {
		super(x, y, l);
		level = l;
		this.bounds = new Bounds(2,4,4,4);
	}

	public void tick() {
		velocity.x += (new Random().nextFloat() - 0.5) / 30f;
		velocity.y += (new Random().nextFloat() - 0.5) / 30f;
		move();
	}

	public void render(Screen screen) {
		screen.drawSprite(new SpriteReference(new Coordinate(32, 56), 8, 8), Math.round(position.x), Math.round(position.y));
	}

	public int getLightRadius() {return 30;}
}