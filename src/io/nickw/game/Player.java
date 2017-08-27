package io.nickw.game;

import java.util.Random;

import javax.swing.KeyStroke;

public class Player extends GameObject {
	float velX = 1f;
	float velY = 0f;
	public Player(int x, int y) {
		super(x,y);
		this.spritePosition = new Coordinate(0,0);
	}
	
	public void tick() {
		if (InputHandler.right.down) velX += 1;
		if (InputHandler.left.down) velX -= 1;
		
		velY += 0.1;
		velX *= 0.5;
		if (this.position.y + velY > Level.height - 16) {
			velY = 0;
		}
		if (this.position.x + velX > Level.width - 16) velX = -velX;
		if (this.position.x + velX <= 0) velX = -velX;
		this.position.x += Math.round(velX);
		this.position.y += Math.round(velY);
	}
}
