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
		alpha = Math.max(Math.min(alpha, 1f), 0f);
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

	public static int getIntensity(int c) {
		int r = (c >> 16) & 0xff;
		int g = (c >> 8) & 0xff;
		int b = (c) & 0xff;
		return (int) (((r / 255.0) * 0.3 + (g / 255.0) * 0.59 + (b / 255.0) * 0.11) * 255);
	}
	
	public static int get(int d) {
		if (d < 0) return 255;
		int r = d / 100 % 10;
		int g = d / 10 % 10;
		int b = d % 10;
		return r * 36 + g * 6 + b;
	}


	public static int desaturate(int c, float f) {
		int r = (c >> 16) & 0xff;
		int g = (c >> 8) & 0xff;
		int b = (c) & 0xff;
		double L = 0.3*r + 0.6*g + 0.1*b;
		int new_r = (int) (r + f * (L - r));
		int new_g = (int) (g + f * (L - g));
		int new_b = (int) (b + f * (L - b));
		return new_r << 16 | new_g << 8 | new_b;
	}

}
