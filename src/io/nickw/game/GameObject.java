package io.nickw.game;

import java.util.Random;

import io.nickw.game.gfx.*;
import io.nickw.game.level.Level;

public class GameObject {
	
	public Coordinate position;
	public long id;
	public SpriteReference sprite;
	private boolean deleted = false;
	public Level level;
	public int order = -3000;
	
	public GameObject(int x, int y, Level l) {
		this.level = l;
		id = new Random().nextLong();
		this.position = new Coordinate(x,y);
		this.sprite = new SpriteReference(new Coordinate(0,0), 8, 8);
	}

	
	public void tick() {
		if (deleted) return;
	}
	
	public void render(Screen screen) {
		if (deleted) return;
		// render coordinates
		int rx = this.position.x;
		int ry = this.position.y;
		screen.drawSprite(sprite, rx, ry);
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
