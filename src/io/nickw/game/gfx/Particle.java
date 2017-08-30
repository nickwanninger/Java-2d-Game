package io.nickw.game.gfx;

import io.nickw.game.Game;
import io.nickw.game.GameObject;
import io.nickw.game.Level;
import io.nickw.game.Vector2;

public class Particle extends GameObject {
	
	Vector2 velocity = new Vector2(0,0);
	long created = 0l;
	long lifetime = 0l;
	int color;
	
	public Particle(int x, int y, Level level, Vector2 impulse, int lt) {
		super(x,y,level);
		velocity = impulse;
		created = System.currentTimeMillis();
		this.lifetime = (long)lt;
		order = 3;
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
		int col = 0xff0044;
		if (isDeleted()) return;
		screen.drawSquare(this.position.x, this.position.y, this.position.x + 2, this.position.y + 2, Color.Adjust(col, 0.9f));
		screen.setPixel(this.position.x, this.position.y, col);
		
	}
}
