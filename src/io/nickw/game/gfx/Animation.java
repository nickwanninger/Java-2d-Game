package io.nickw.game.gfx;

import io.nickw.game.Coordinate;

public class Animation {
	
	public SpriteReference[] sprites = new SpriteReference[] {};
	public int currentFrame = 0;
	public int tickCount = 0;
	public int frameRate = 15;
	public int loopCount = 0;
	
	public Animation(int frames, Coordinate startTile) {
		sprites = new SpriteReference[frames];
		// loop over the frames and create the
		// references to the sheet for each one.
		for (int i = 0; i < frames; i++) {
			// the x and y coordinate of the frame on the sheet
			int x = startTile.x + i * 8;
			int y = startTile.y; // will be the same until wrapping animations are implemented
			Coordinate sCoord = new Coordinate(x,y);
			sprites[i] = new SpriteReference(sCoord, 8, 8);
		}
	}
	
	public SpriteReference GetCurrentFrame() {
		return sprites[currentFrame];
	}

	public void tick() {
		// Increment the tick by 1 every tick...
		tickCount++;
		updateFrame();
	}
	
	public void updateFrame() {
		/**
		 * Divide by 4 here because I want
		 * the animations to run at 15fps
		 * 60/15 = 4 
		 */
		currentFrame = (int)(tickCount / (60f / frameRate)) % sprites.length;
		loopCount = (int) ((tickCount / (60f / frameRate)) / sprites.length);
	}
	public void reset() {
		tickCount = 0;
		updateFrame();
	}

}
