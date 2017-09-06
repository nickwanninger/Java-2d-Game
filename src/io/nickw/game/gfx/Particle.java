package io.nickw.game.gfx;

import io.nickw.game.Coordinate;
import io.nickw.game.Game;
import io.nickw.game.GameObject;
import io.nickw.game.Vector2;
import io.nickw.game.level.Level;

public class Particle extends GameObject {
	
	Vector2 velocity = new Vector2(0,0);
	long created = 0l;
	long lifetime = 0l;
	int color;
	SpriteReference sprite = new SpriteReference(new Coordinate(0, 8 * 5), 8, 8);
	
	public Particle(int x, int y, Level level, Vector2 impulse, int lt, int color) {
		super(x,y,level);
		this.color = color;
		velocity = impulse;
		created = System.currentTimeMillis();
		this.lifetime = (long)lt;
		order = 4;
	}
	
	public void tick () {
		if (isDeleted()) return;
		if (System.currentTimeMillis() >= created + lifetime) {
			this.destroy();
			return;
		}
		velocity.y += Game.gravity;
		this.position.x += Math.round(velocity.x);
		this.position.y += Math.round(velocity.y);
	}
	
	public void render (Screen screen) {
		if (isDeleted()) return;
//		screen.setPixel(this.position.x, this.position.y, color);
		screen.drawSprite(sprite, this.position.x, this.position.y);
		
	}
}
