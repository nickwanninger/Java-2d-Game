package io.nickw.game.gfx;

import io.nickw.game.Coordinate;
import io.nickw.game.Game;
import io.nickw.game.level.Level;

public class Screen {

	public int width;
	public int height;
	public Sprite sheet;
	public int[][] sprites;
	public Coordinate offset = new Coordinate(0,0);

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


//	public void setPixel(double x, double y, int c) {
//
//		for (int rx = (int) Math.floor(x); rx <= Math.ceil(x); rx++) {
//			for (int ry = (int) Math.floor(y); ry <= Math.ceil(y); ry++) {
//				double px = 1 - Math.abs(x - rx);
//				double py = 1 - Math.abs(y - ry);
//				double alpha = px * py;
//				int tcolor = getPixel(rx, ry);
//				int color = Color.lerp(tcolor, c, (float) alpha);
//				setPixel(rx, ry, color);
//			}
//		}
//		setPixel((int)Math.round(x), (int)Math.round(y), c);
//
//	}

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
		for (int sX = 0; sX < s.width; sX++) {
			for (int sY = 0; sY < s.height; sY++) {
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
	}

//	public void drawSpriteRotate(SpriteReference s, int x, int y, double r, int flipBits) {
//		boolean mirrorX = (0x01 & flipBits) > 0;
//		boolean mirrorY = (0x02 & flipBits) > 0;
//		int w = s.width;
//		int h = s.height;
//		int sheetX = s.pos.x;
//		int sheetY = s.pos.y;
//		int pixelLen = s.width * s.height;
//
//		float cos = (float)Math.cos(r);
//		float sin = (float)Math.sin(r);
//		int hw = (int) Math.round(s.width / 2.0);
//		int hh = (int) Math.round(s.height / 2.0);
//
//		for (int sX = 0; sX < w; sX ++) {
//			for (int sY = 0; sY < h; sY ++) {
//				int pixel = sheet.GetPixel(sX + sheetX, sY + sheetY);
//				// if the color of the pixel isn't the null color... draw it to the screen
//				if (pixel != Sprite.transparent_value) {
//					double rx = ((sX - hw) * cos - (sY - hh) * sin);
//					double ry = ((sX - hw) * sin + (sY - hh) * cos);
//					double dx = rx + x;
//					double dy = ry + y;
//					if (mirrorX)
//						dx = -rx + x;
//					if (mirrorY)
//						dy = -ry + y;
//
//					double tx = dx - offset.x + hw;
//					double ty = dy - offset.y + hh;
//					setPixel((int) tx, (int) ty, pixel);
//
//				}
//			}
//		}
//	}

	public boolean mouseWithin(int x0, int y0, int x1, int y1) {
		 int mx = Game.mouseX;
		 int my = Game.mouseY;
		 return (mx >= x0 && my >= y0 && mx < x1 && my < y1);
	}

	public void drawSpriteGUI(SpriteReference s, int x, int y) {
		drawSprite(s, x + offset.x, y + offset.y);
	}
	public void drawSpriteGUI(SpriteReference s, int x, int y, int scale) {
		drawSprite(s, x + offset.x, y + offset.y, scale);
	}
}
