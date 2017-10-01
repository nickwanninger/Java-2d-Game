package io.nickw.game.gfx;

public class Font {
	private static Sprite fontData;
	private static String chars = " ABCDEFGHIJKLMNOPQRSTUVWZYZ:;,.!?><()_/\\0123456789%";
	
	public Font() {
		Font.fontData = new Sprite("/font.png");
	}
	
	public static void drawText(Screen screen, String text, int x, int y) {
		drawText(screen, text, x, y, 0x000000);
	}
	
	
	public static void drawWithFrame(Screen screen, String msg, int x, int y) {
		drawWithFrame(screen, msg, x, y, 0xffffff, 0xff000000);
	}

	public static void drawWithFrame(Screen screen, String msg, int x, int y, int col, int bgCol) {
		screen.drawSquare(x, y, msg.length() * 6 + 1, y + 7, bgCol);
		drawText(screen, msg, x + 1, y + 1, col);
	}
	public static void drawText(Screen screen, String msg, int x, int y, int col) {
		msg = msg.toUpperCase();
		for (int l = 0; l < msg.length(); l++) {
			int ix = chars.indexOf(msg.charAt(l));
			int offset = ix * 6;
			if (ix >= 0) {
				for (int i = 0; i < 36; i++) {
					int sx = offset + (i / 6);
					int sy = i % 6;
					int pixel = fontData.GetPixel(sx, sy);
					if (pixel == 0xffffff) {
						 screen.setPixel(x + i / 6 + l * 6, y + sy, col);
					}
				}
			}
			
		}
	}
}
