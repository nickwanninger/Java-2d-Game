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


	static int lerpVal(int v1, int v2, float alpha) {
		return (int) (v1 + alpha * (v2 - v1));
	}

	public static int lerp(int c1, int c2, float alpha) {
		int r1 = (c1 >> 16) & 0xff;
		int g1 = (c1 >> 8) & 0xff;
		int b1 = (c1) & 0xff;
		int r2 = (c2 >> 16) & 0xff;
		int g2 = (c2 >> 8) & 0xff;
		int b2 = (c2) & 0xff;
		int r = lerpVal(r1, r2, alpha);
		int g = lerpVal(g1, g2, alpha);
		int b = lerpVal(b1, b2, alpha);
		return r << 16 | g << 8 | b;
	}
	
	public static int get(int d) {
		if (d < 0) return 255;
		int r = d / 100 % 10;
		int g = d / 10 % 10;
		int b = d % 10;
		return r * 36 + g * 6 + b;
	}

}
