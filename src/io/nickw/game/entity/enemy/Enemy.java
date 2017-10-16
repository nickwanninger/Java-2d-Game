package io.nickw.game.entity.enemy;

import io.nickw.game.Bounds;
import io.nickw.game.Coordinate;
import io.nickw.game.Direction;
import io.nickw.game.Vector2;
import io.nickw.game.entity.Entity;
import io.nickw.game.entity.Player;
import io.nickw.game.gfx.Animation;
import io.nickw.game.gfx.Raycaster;
import io.nickw.game.gfx.Screen;
import io.nickw.game.level.Level;

public class Enemy extends Entity {

	float movespeed = 0.3f;
	public long gainedLOS = System.currentTimeMillis();
	public long lostLOS = System.currentTimeMillis();
	Animation animation = new Animation(4, new Coordinate(0,248), 8, 8);

	public Enemy(float x, float y, Level l) {
		super(x, y, l);
		this.level = l;
		bounds = new Bounds(2, 3, 4,5);
		animation.frameRate = 16;
	}

	public void tick() {

		Vector2 tVector = getPlayerVector();
		float mag = Vector2.getMagnitude(tVector);
		int maxDistance = 70;

		boolean seesPlayer = false;


		if (mag > 4 && mag < maxDistance) {
			Vector2 offset = new Vector2(4,4);
			Vector2 start = Vector2.add(position, offset);
			Vector2 end = Vector2.add(Vector2.add(position, tVector), offset);
			Raycaster.Result castResult = Raycaster.run(start, end, this.level, 0);

			if (!castResult.hit) {
				seesPlayer = true;
				gainedLOS = System.currentTimeMillis();
				Vector2 normalizedVector = Vector2.normalize(tVector);
				Vector2 finalVector = Vector2.multiply(normalizedVector, movespeed);
				velocity.add(finalVector);
			}

		}

		// random walk around
		if ((mag > maxDistance || !seesPlayer) && Math.random() < 0.5f) {
			float ox = (float) ((Math.random() - 0.5) * (movespeed / 3f));
			float oy = (float) ((Math.random() - 0.5) * (movespeed / 3f));
			velocity.add(new Vector2(ox, oy));
		}


		boolean moving = false;
		if (Vector2.getMagnitude(velocity) > 0.05) {
			moving = true;
		}
		if (moving) {
			animation.tick();
		}
		move();
		if (mag <= maxDistance) {
			velocity = new Vector2();
		} else {
			velocity = Vector2.clampMagnitude(velocity, movespeed);
		}

	}

	public void render(Screen screen) {
		int flip = dir == Direction.EAST ? 0x00 : 0x01;
		screen.drawSprite(animation.GetCurrentFrame(), Math.round(position.x), Math.round(position.y), flip);
//		bounds.render(screen, position);
	}

	public Vector2 getPlayerVector() {
		Player p = level.getPlayer();
		float px = p.position.x;
		float py = p.position.y;
		float dx = px - position.x;
		float dy = py - position.y;
		return new Vector2(dx,dy);
	}

}