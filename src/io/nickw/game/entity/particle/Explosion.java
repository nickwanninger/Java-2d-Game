package io.nickw.game.entity.particle;

import io.nickw.game.Coordinate;
import io.nickw.game.Vector2;
import io.nickw.game.entity.Entity;
import io.nickw.game.gfx.Animation;
import io.nickw.game.gfx.Screen;
import io.nickw.game.level.Level;
import io.nickw.game.sound.Sounds;

public class Explosion extends Entity {
	public int tickCount = 0;
	Animation animation;
	public Explosion(float x, float y, Level l) {

		super(x, y, l);
		Sounds.explosion.play();
		animation = new Animation(10, new Coordinate(0, 312), 24, 24);
		animation.frameRate = 32;
		for (int i = 0; i < 50; i++) {
			float sx = (float) Math.sin(Math.random()) * 2;
			float sy = (float) Math.sin(Math.random()) * 2;
			Smoke s = new Smoke(position.x + sx, position.y + sy, l);
			float vx = (float) (Math.random() - 0.5) * 3;
			float vy = (float) (Math.random() - 0.5) * 3;
			s.velocity = new Vector2(vx, vy);
			l.addObject(s);
		}
	}

	public void tick() {
		tickCount++;
		animation.tick();
		if (tickCount >= 19) destroy();
		position.x += velocity.x;
		position.y += velocity.y;
	}

	public void render(Screen screen) {
		// draw offset to the location because the sprite is larger than 8x8
		screen.drawSprite(animation.GetCurrentFrame(), Math.round(position.x - 8), Math.round(position.y - 8));
	}

	public int getLightRadius() {
		return (int) (70 + (Math.random() * 20));
	}
}