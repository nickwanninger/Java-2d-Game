package io.nickw.game.entity.projectile;

import io.nickw.game.Bounds;
import io.nickw.game.Coordinate;
import io.nickw.game.Game;
import io.nickw.game.Vector2;
import io.nickw.game.entity.Entity;
import io.nickw.game.entity.particle.Burst;
import io.nickw.game.entity.particle.Explosion;
import io.nickw.game.entity.particle.Smoke;
import io.nickw.game.gfx.Screen;
import io.nickw.game.gfx.SpriteReference;
import io.nickw.game.level.Level;
import io.nickw.game.util.Cooldown;

public class Projectile extends Entity {

	public int lingerTime = 500;
	public Cooldown cooldown;
	public long created;

	public Projectile(float x, float y, Vector2 v, Level l) {
		super(x, y, l);
		this.level = l;
		this.velocity = v;
		this.sprite = new SpriteReference(new Coordinate(0, 88), 8, 8);
		this.bounds = new Bounds(3, 3, 3, 3);
		cooldown = new Cooldown(60 + (int) (Math.random() * 30));
		created = System.currentTimeMillis();
	}



	public void tick() {
		move();
		float chaos = 0.0f;
		velocity.x += ((Math.random() - 0.5f) * chaos);
		velocity.y += ((Math.random() - 0.5f) * chaos);
		if (Math.random() > 0.5f) {
			float sx = (float) Math.sin(Math.random()) * 2;
			float sy = (float) Math.sin(Math.random()) * 2;
			level.addObject(new Smoke(position.x + sx + 2, position.y + sy + 2, level));
		}
		cooldown.tick();
//		if(cooldown.isDone()) {
//			destroy();
//			explode();
//		}
	}

	public void explode() {
		level.addObject(new Explosion(position.x + 2, position.y + 2, level));
	}

	public void burst() {
		level.addObject(new Burst(position.x, position.y, level));
	}

	public int getLightRadius() {
		return (int) (Math.sin(Math.random()) * 30);
	}

	public double getMovementAngle() {
		return Math.atan2(velocity.y, velocity.x);
	}

	@Override
	public void hitWall() {
		velocity.x = 0;
		velocity.y = 0;
		destroy();
		burst();
//		explode();
	}
}