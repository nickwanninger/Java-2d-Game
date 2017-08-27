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
		
		pixels = image.getRGB(0, 0, width, height, null, 0, width);
		for (int i = 0; i < pixels.length; i++) {
            int in = pixels[i];
            int col = in;
            if (in == 0xffe476ff || in == 0xff8e49a2) col = -1;
            pixels[i] = col;
         }
		
	}
	
	public int GetPixel(int x, int y) {
		return pixels[y * width + x];
	}
}
