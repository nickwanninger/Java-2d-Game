package io.nickw.game.gfx;

import io.nickw.game.Coordinate;
import io.nickw.game.Game;
import io.nickw.game.level.Level;

public class Screen {

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
		pixels = new int[width * height];
	}

	public void setSize(int w, int h) {
		pixels = new int[w * h];
	}

	public void clear(int color) {
		for (int i = 0; i < pixels.length; i++) {
			pixels[i] = color;
		}
	}

	public void setOffset(int x, int y) {
		offset = new Coordinate(x, y);
	}


	// quick setting of a certain pixel
	public void setPixel(int x, int y, int color) {
		if (x >= 0 && x < width && y >= 0 && y < height) {
			pixels[y * width + x] = color;
		}

	}

	public int getPixel(int x, int y) {
		if (x >= 0 && x < width && y >= 0 && y < height) {
			return pixels[y * width + x];
		}
		return 0;
	}


	public void drawLine(int x0, int y0, int x1, int y1, int col) {
		double dx = x1 - x0;
		double dy = y1 - y0;
		double stepCount = Math.abs(dx) > Math.abs(dy) ? Math.abs(dx) : Math.abs(dy);
		double stepX = dx / stepCount;
		double stepY = dy / stepCount;
		double x = x0;
		double y = y0;
		for (int i = 0; i < stepCount + 1; i++) {
			int drawx = (int) Math.round(x) - offset.x;
			int drawy = (int) Math.round(y) - offset.y;
			setPixel(drawx, drawy, col);
			x = x + stepX;
			y = y + stepY;
		}
	}

	public void drawSquare(int x1, int y1, int x2, int y2, int col) {
		for (int x = x1; x < x2; x++) {
			for (int y = y1; y < y2; y++) {
				setPixel(x, y, col);
			}
		}
	}


	public void drawSprite(SpriteReference s, int x, int y) {
		drawSprite(s, x, y, 0x00);
	}

	public void drawSprite(SpriteReference s, int x, int y, int flipBits) {
		boolean mirrorX = (0x01 & flipBits) > 0;
		boolean mirrorY = (0x02 & flipBits) > 0;
		int tileX = s.pos.x;
		int tileY = s.pos.y;
		int pixelLen = s.width * s.height;
		// loop over the 256 pixels in each sprite
		for (int i = 0; i < pixelLen; i++) {
			int sX = i % s.width;
			int sY = i / s.height;
			int pixel = sheet.GetPixel(sX + tileX, sY + tileY);
			// if the color of the pixel isn't the null color... draw it to the screen
			if (pixel != Sprite.transparent_value) {
				double dx = sX + x;
				double dy = sY + y;
				if (mirrorX) {
					dx = (s.width - 1) - sX + x;
				}
				if (mirrorY) {
					dy = (s.height - 1) - sY + y;
				}
				setPixel((int) dx - offset.x, (int) dy - offset.y, pixel);
			}
		}
	}

	public void drawSpriteGUI(SpriteReference s, int x, int y) {
		drawSprite(s, x + offset.x, y + offset.y);
	}




}
