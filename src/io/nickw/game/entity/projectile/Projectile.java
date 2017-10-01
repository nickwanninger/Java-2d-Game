package io.nickw.game.entity.projectile;

import io.nickw.game.Bounds;
import io.nickw.game.Coordinate;
import io.nickw.game.Vector2;
import io.nickw.game.entity.Entity;
import io.nickw.game.entity.particle.Smoke;
import io.nickw.game.gfx.SpriteReference;
import io.nickw.game.level.Level;
import io.nickw.game.sound.Sound;

public class Projectile extends Entity {

	public int lingerTime = 500;
	public long creationTime = 0l;

	public Projectile(float x, float y, Vector2 v, Level l) {
		super(x, y, l);
		this.level = l;
		this.velocity = v;
		this.sprite = new SpriteReference(new Coordinate(0, 88), 8, 8);
		this.bounds = new Bounds(3, 3, 3, 3);
		creationTime = System.currentTimeMillis();
	}

	public int getLightRadius() {
		return (int) (Math.sin(Math.random()) * 70);
	}

	public void tick() {
		move();
		float chaos = 1.0f;
		velocity.x += (Math.random() - 0.5f) * chaos;
		velocity.y += (Math.random() - 0.5f) * chaos;
		if (Math.random() > 0.5f) {
			float sx = (float) Math.sin(Math.random()) * 2;
			float sy = (float) Math.sin(Math.random()) * 2;
			level.addObject(new Smoke(position.x + sx + 2, position.y + sy + 2, level));
		}

		if (System.currentTimeMillis() - creationTime >= lingerTime) {
			destroy();
			explode();
		}

	}

	public void explode() {
		Sound.poof.play();
		int smokeCount = (int) (20 + Math.random() * 5);
		for (int i = 0; i < smokeCount; i++) {
			float sx = (float) Math.sin(Math.random()) * 2;
			float sy = (float) Math.sin(Math.random()) * 2;
			Smoke s = new Smoke(position.x + sx + 2, position.y + sy + 2, level);
			float vx = (float) (Math.random() - 0.5) * 1;
			float vy = (float) (Math.random() - 0.5) * 1;
			s.velocity = new Vector2(vx, vy);
			level.addObject(s);
		}
	}

	@Override
	public void hitWall() {
		destroy();
		explode();

	}
}