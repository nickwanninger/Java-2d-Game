package io.nickw.game;

import java.util.Random;

public class Player extends GameObject {
	
	Random rng = new Random(100);
	
	Vector2 velocity = new Vector2(0,0);
	
	public Player(int x, int y, Level level) {
		super(x,y, level);
		order = 2;
		this.spritePosition = new Coordinate(1,0);
	}
	
	public void tick() {
		
		velocity.y += Game.gravity;
		
		int sx = 0;
		int sy = 0;
		
		float moveSpeed = 1f;
		if (InputHandler.right.down) sx += moveSpeed;
		if (InputHandler.left.down) sx -= moveSpeed;
		if (InputHandler.down.down) sy += moveSpeed;
		if (InputHandler.up.down) sy -= moveSpeed;
		
		if (this.position.x + sx + 16 > Level.width) sx = 0;
		if (this.position.x + sx + 1<= 0) sx = 0;
		
		if (this.position.y + sy + 16 > Level.height) sy = 0;
		if (this.position.y + sy + 1 <= 0) sy = 0;
	
		this.position.x += sx;
		this.position.y += sy;
		
		velocity.x *= 0.8;
	}
	
}
