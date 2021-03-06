package io.nickw.game.gfx;

import io.nickw.game.Coordinate;
import io.nickw.game.GameObject;
import io.nickw.game.level.Level;

public class LightingEngine implements Runnable {
	public Level level;
	int width = 0;
	int height = 0;
	public int[] pixels;
	public int[] dither = new int[]{0, 8, 2, 10, 12, 4, 14, 6, 3, 11, 1, 9, 15, 7, 13, 5,};
	private long lastDrawTime = 0;
	public long lastDrawDelay = 0;
	Thread thread;
	Coordinate offset = new Coordinate(0,0);
	public boolean shouldRender = true;


	public LightingEngine(int w, int h, Level l) {
		this.width = w;
		this.height = h;
		this.level = l;
		this.pixels = new int[w * h];
		this.thread = new Thread(this);
		this.thread.start();
	}

	public void setOffset(int x, int y) {
		this.offset = new Coordinate(x, y);
	}

	public void run() {
		int[] buffer = new int[pixels.length];
		int[] ownership = new int[pixels.length];
		while (true) {
			lastDrawTime = System.currentTimeMillis();
			for (int i = 0; i < buffer.length; i++) buffer[i] = 0;
			for (int i = 0; i < ownership.length; i++) ownership[i] = -1;
			if (shouldRender) {
				for	(int i = 0; i < level.objects.size(); i++) {
					GameObject o = level.objects.get(i);
					if (!(o == null)) {
						LightingType type = o.lightingType;
						int radius = o.getLightRadius();
						float intensity = o.getLightIntensity();
						int px = Math.round(o.position.x + 4);
						int py = Math.round(o.position.y + 4);

						if (Math.abs(radius) > 0) {
							if (type == LightingType.Fancy) {
								renderPointLightAdvanced(px, py, radius, intensity, buffer);
							} else if (type == LightingType.Fast) {
								renderPointLightBasic(px, py, radius, buffer);
							}
						}
					} else {
						System.out.println("Object doesnt exist, skipping");
					}

				}
				pixels = buffer.clone();

				lastDrawDelay = (System.nanoTime() - lastDrawTime);
//				try {
//					Thread.sleep(2);
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
			}
		}

	}

	void clearPixels() {
		for (int i = 0; i < pixels.length; i++) pixels[i] = 0;
	}



	public void renderPointLightBasic(int x, int y, int r, int[] pixels) {

		x -= offset.x;
		y -= offset.y;
		int x0 = x - r;
		int x1 = x + r;
		int y0 = y - r;
		int y1 = y + r;
		if (x0 < 0) {
			x0 = 0;
		}
		if (y0 < 0) {
			y0 = 0;
		}
		if (x1 > width) {
			x1 = width;
		}
		if (y1 > height) {
			y1 = height;
		}
		for (int yy = y0; yy < y1; yy++) {
			int yd = yy - y;
			yd = yd * yd;
			for (int xx = x0; xx < x1; xx++) {
				int xd = xx - x;
				int dist = xd * xd + yd;
				if (dist <= r * r) {
					int br = 255 - dist * 255 / (r * r);
					if (br > getPixel(xx, yy, pixels)) {
						setPixel(xx, yy, br, pixels);
					}
				}
			}
		}
	}


	public void renderPointLightAdvanced(int x0, int y0, int r, float intensity, int[] pixels) {
		double tau = 2 * Math.PI;
		double drawCount = (double)(r * 2);
		double radius = (double) r;
		// run around the circle in 8 locations
		for (double i = 0; i < tau; i += tau / drawCount) {
			double rx = radius * Math.sin(i);
			double ry = radius * Math.cos(i);

			Coordinate start = new Coordinate(x0, y0);
			Coordinate end = new Coordinate((int) (x0 + rx),(int) (y0 + ry));
			end = Raycaster.run(start, end, level, 1).position.toCoordinate();

			int x1 = end.x;
			int y1 = end.y;

			double dx = x1 - x0;
			double dy = y1 - y0;
			double stepCount = Math.abs(dx) > Math.abs(dy) ? Math.abs(dx) : Math.abs(dy);
			double stepX = dx / stepCount;
			double stepY = dy / stepCount;
			double x = x0;
			double y = y0;
			for (int j = 0; j < stepCount + 1; j++) {
				int xx = (int) Math.round(x) - offset.x;
				int yy = (int) Math.round(y) - offset.y;

				int a = ((int) Math.round(x)) - x0;
				int b = ((int) Math.round(y)) - y0;
				double dist = Math.sqrt(Math.pow(a,2) + Math.pow(b,2));
				int br = (int) ((255 - (255 * (dist / radius))) * intensity);

				for (int ox = -1; ox <= 1; ox++) {
					for (int oy = -1; oy <= 1; oy++) {
						int fx = xx + ox;
						int fy = yy + oy;
						int c = (int) (br * ((ox == 0 && oy == 0) ? 1 : 0.25f));
						if (br > getPixel(fx, fy, pixels)) {
							setPixel(fx, fy, br, pixels);
						}
					}
				}

				x = x + stepX;
				y = y + stepY;
			}
		}
	}




	public void setPixel(int x, int y, int color, int[] pixels) {
		if (x >= 0 && x < width && y >= 0 && y < height) {
			pixels[y * width + x] = color;
		}
	}

	public int getPixel(int x, int y, int[] pixels) {
		if (x >= 0 && x < width && y >= 0 && y < height) {
			return pixels[y * width + x];
		}
		return 0;
	}

	public void overlayOntoScreen(Screen screen) {

		for (int i = 0; i < height * width; i++) {
			int x = i / width;
			int y = i % width;
			int dx = (int) (x + offset.x) & 3;
			int dy = (int) (y + offset.y) & 3;
			float intensity = Color.getIntensity(pixels[i]);
			float alpha = pixels[i] / 255f + 0.05f;

//			screen.pixels[i] = pixels[i];

			if (screen.pixels[i] != 0xd95763) {
				screen.pixels[i] = Color.lerp(0x000000, screen.pixels[i], alpha);

				if (pixels[i] / 10 <= dither[dx + dy * 4]) {
					screen.pixels[i] = Color.lerp(0x0e0d15, screen.pixels[i], 0.8f);
				}
			}


		}
	}
}