package io.nickw.game;

import java.util.ArrayList;
import java.util.Random;

import io.nickw.game.gfx.*;

public class GameObject {
	
	public Coordinate position;
	public long id;
	public Coordinate spritePosition;
	
	public GameObject(int x, int y) {
		id = new Random().nextLong();
		this.position = new Coordinate(x,y);
	}
	
	public void tick() {
		
	}
	
	public void render(Screen screen) {
		// render coordinates
		int rx = (int)this.position.x;
		int ry = (int)this.position.y;
		screen.drawSprite(spritePosition, rx, ry);
	}
	
	
}
