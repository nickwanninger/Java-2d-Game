package io.nickw.game.gfx;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

import io.nickw.game.Coordinate;

public class Screen {
	
	public int xOffset = 0;
	public int yOffset = 0;
	public int width;
	public int height;
	public Sprite sheet;
	public int[][] sprites;
	
	public int[] pixels;
	
	public Screen(int width, int height, Sprite sheet) {
		this.width = width;
		this.height = height;
		this.sheet = sheet;
		pixels = new int[width*height];
		int pixelsInSprite = (sheet.width / 16) * (sheet.height / 16);
	}
	
	public void setSize(int w, int h) {
		pixels = new int[w*h];
	}
	public void clear(int color) {
		for(int i = 0; i < pixels.length; i++) {
			pixels[i] = color;
		}
	}
	public Image renderToImage() {
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        WritableRaster raster = (WritableRaster) image.getData();
        raster.setPixels(0,0,width,height,pixels);
        return image;
	}
	
	public void drawSprite(Coordinate spritePosition, int x, int y) {
		int xOffset = spritePosition.x * 16;
		int yOffset = spritePosition.y * 16;
		// loop over the 256 pixels in each sprite
		for	(int i = 0; i < 256; i++) {
			int spriteX = i % 16;
			int spriteY = i / 16;
			int pixel = sheet.GetPixel(spriteX + xOffset, spriteY + yOffset);
			// if the color of the pixel isn't the null color... draw it to the screen
			if (pixel != -1 ) {
				int drawIndex = (y + spriteY) * width + x + spriteX;
				if (drawIndex < pixels.length && drawIndex > 0) {
					pixels[drawIndex] = pixel;
				}
				
			}
		}
	}
}
