package io.nickw.game.entity;

import io.nickw.game.Coordinate;
import io.nickw.game.gfx.Animation;
import io.nickw.game.gfx.Screen;
import io.nickw.game.level.Level;

public class Smoke extends Entity {
	public int tickCount = 0;
	Animation animation;
	public Smoke(float x, float y, Level l) {
		super(x, y, l);
		animation = new Animation(4, new Coordinate(0, 72), 2, 2);
		animation.frameRate = 16;
	}

	public void tick() {
		tickCount++;
		animation.tick();
		if (tickCount >= 15) destroy();

	}

	public void render(Screen screen) {
		screen.drawSprite(animation.GetCurrentFrame(), Math.round(position.x), Math.round(position.y));
	}
}