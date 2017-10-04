package io.nickw.game;

import java.util.Random;

import io.nickw.game.gfx.*;
import io.nickw.game.level.Level;

public class GameObject {

	public Vector2 position;
	public long id;
	public SpriteReference sprite;
	public boolean deleted = false;
	public Level level;
	public int order = -1;
	public LightingType lightingType = LightingType.Fancy;
	
	public GameObject(float x, float y, Level l) {
		this.level = l;
		this.id = new Random().nextLong();
		this.position = new Vector2(x,y);
		this.sprite = new SpriteReference(new Coordinate(0,0), 8, 8);
	}

	
	public void tick() {
		if (deleted) return;
	}
	
	public void render(Screen screen) {
		if (deleted) return;
		// render coordinates
		int rx = Math.round(this.position.x);
		int ry = Math.round(this.position.y);
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

	public int getLightRadius() {return 0;}

	public int getLightColor() {
		return 0xffffff;
	}

	public float getLightIntensity() {return 1f;}

	
	
}
