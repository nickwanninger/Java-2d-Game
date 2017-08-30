package io.nickw.game.gfx;

import io.nickw.game.Coordinate;

public class Screen {
	
	public int xOffset = 0;
	public int yOffset = 0;
	public int width;
	public int height;
	public Sprite sheet;
	public int[][] sprites;
	public Coordinate offset;
	
	public int[] pixels;
	
	public Screen(int width, int height, Sprite sheet) {
		
		this.width = width;
		this.height = height;
		this.sheet = sheet;
		pixels = new int[width*height];
	}
	
	public void setSize(int w, int h) {
		pixels = new int[w*h];
	}
	public void clear(int color) {
		for(int i = 0; i < pixels.length; i++) {
			pixels[i] = color;
		}
	}
	
	public Coordinate setOffset(int x, int y) {
		offset = new Coordinate(x, y);
		return offset;
	}
	
	// quick setting of a certain pixel
	public void setPixel(int x, int y, int color) {
		if (x >= 0 && x < width && y >= 0 && y < height) {
			pixels[y * width + x] = color;
		}
		
	}
	
	public void drawSquare(int x1, int y1, int x2, int y2, int col) {
		for (int x = x1; x < x2; x++ ) {
			for (int y = y1; y < y2; y++ ) {
				setPixel(x,y,col);
			}
		}
	}
	
	public void drawSprite(Coordinate spritePosition, int x, int y) {
		drawSprite(spritePosition, x, y, 0);
	}
	
	public void drawSprite(Coordinate spritePosition, int x, int y, float r) {
		int tileX = spritePosition.x * 16;
		int tileY = spritePosition.y * 16;
		// loop over the 256 pixels in each sprite
		for	(int i = 0; i < 256; i++) {
			int spriteX = i % 16;
			int spriteY = i / 16;
			int pixel = sheet.GetPixel(spriteX + tileX, spriteY + tileY);
			// if the color of the pixel isn't the null color... draw it to the screen
			if (!(pixel == 0xffe476ff || pixel == 0xff8e49a2)) {
				// rotation formula and info from: https://homepages.inf.ed.ac.uk/rbf/HIPR2/rotate.htm
				int x1 = spriteX;
				int y1 = spriteY;
//				int x0 = (x1 - 8);
//				int y0 = (y1 - 8);
				int x0 = x1;
				int y0 = y1;
				
//				int scale = 1;
//				double dx = ( Math.cos(r) * x0 - Math.sin(r) * y0 ) * scale + x;
//				double dy = ( Math.sin(r) * x0 + Math.cos(r) * y0 ) * scale + y;
				double dx = x0 + x;
				double dy = y0 + y;
				
				setPixel((int)dx - offset.x, (int)dy - offset.y, pixel);
			}
		}
	}
}
