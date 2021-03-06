package io.nickw.game.gfx;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Sprite {
	public String path;
	public int width;
	public int height;
	public int[] pixels;
	BufferedImage image;

	public static int transparent_value = -1;
	
	public Sprite(String path) {
		BufferedImage image = null;
		
		try {
			image = ImageIO.read(Sprite.class.getResource(path));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if (image == null) {
			return;
		}

		this.path = path;
		this.width = image.getWidth();
		this.height = image.getHeight();
		this.image = image;
		int colorDepth = 4;
		pixels = image.getRGB(0, 0, width, height, null, 0, width);
		for (int i = 0; i < pixels.length; i++) {
			if (pixels[i] == 0x00000000) {
				pixels[i] = transparent_value;
			} else {
				pixels[i] = pixels[i] & 0xffffff;
			}
		}
	}
	
	public int GetPixel(int x, int y) {
		if (x >= 0 && x < width && y >= 0 && y < height) {
			return pixels[y * width + x];
		} else {
			return -1;
		}
		
	}
}
