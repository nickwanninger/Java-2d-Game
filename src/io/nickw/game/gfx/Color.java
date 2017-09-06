package io.nickw.game.gfx;

public class Color {

	public static int Adjust(int c, float d) {
		int r = (c >> 16) & 0xff;
		int g = (c >> 8) & 0xff;
		int b = (c) & 0xff;
		r = (int) (r * d);
		g = (int) (g * d);
		b = (int) (b * d);
		return r << 16 | g << 8 | b;	
	}
	
	public static int get(int a, int b, int c, int d) {
		return (get(d) << 24) + (get(c) << 16) + (get(b) << 8) + (get(a));
	}
	
	public static int get(int d) {
		if (d < 0) return 255;
		int r = d / 100 % 10;
		int g = d / 10 % 10;
		int b = d % 10;
		return r * 36 + g * 6 + b;
	}

}
