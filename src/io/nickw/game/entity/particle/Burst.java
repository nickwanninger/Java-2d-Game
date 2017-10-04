package io.nickw.game.entity.particle;

import io.nickw.game.Vector2;
import io.nickw.game.entity.Entity;
import io.nickw.game.gfx.Animation;
import io.nickw.game.gfx.Screen;
import io.nickw.game.level.Level;
import io.nickw.game.sound.Sounds;

public class Burst extends Entity {
	public int tickCount = 0;
	Animation animation;
	public Burst(float x, float y, Level l) {

		super(x, y, l);
		Sounds.hit.play(0.1f);
		for (int i = 0; i < 10; i++) {
			Smoke s = new Smoke(position.x + 4, position.y + 4, l);
			float vx = (float) (Math.random() - 0.5) * 2;
			float vy = (float) (Math.random() - 0.5) * 2;
			s.velocity = new Vector2(vx, vy);
			l.addObject(s);
		}
	}

	public void tick() {
		tickCount++;
		if (tickCount >= 19) destroy();
		position.x += velocity.x;
		position.y += velocity.y;
	}

	public void render(Screen screen) {
	}

	public int getLightRadius() {
		return (int) (10 + (Math.random() * 20));
	}
}