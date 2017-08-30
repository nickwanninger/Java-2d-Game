package io.nickw.game;

import java.util.Random;

import io.nickw.game.gfx.*;

public class GameObject {
	
	public Coordinate position;
	public long id;
	public Coordinate spritePosition;
	private boolean deleted = false;
	public Level level;
	public int order = -3000;
	
	public GameObject(int x, int y, Level l) {
		this.level = l;
		id = new Random().nextLong();
		this.position = new Coordinate(x,y);
		this.spritePosition = new Coordinate(0,0);
	}

	
	public void tick() {
		if (deleted) return;
	}
	
	public void render(Screen screen) {
		if (deleted) return;
		// render coordinates
		int rx = (int)this.position.x;
		int ry = (int)this.position.y;
		screen.drawSprite(spritePosition, rx, ry);
	}
	
	public boolean equals(GameObject other) {
		return other.id == id;
	}
	
	public void destroy() {
		deleted = true;
		level.remove(this);
	}
	
	public boolean isDeleted() {
		return this.deleted;
	}
	
	
}
