package io.nickw.game.gfx;

public class Font {
	private static Sprite fontData;
	private static String chars = " !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~";
	
	public Font() {
		Font.fontData = new Sprite("/font.png");
	}
	
	public static void drawText(Screen screen, String text, int x, int y) {
		drawText(screen, text, x, y, 0xff000000);
	}
	
	
	public static void drawWithFrame(Screen screen, String msg, int x, int y) {
		drawWithFrame(screen, msg, x, y, 0xffffffff, 0xff000000);
	}
	public static void drawWithFrame(Screen screen, String msg, int x, int y, int col, int bgCol) {
		screen.drawSquare(x, y, msg.length() * 8 + 2, y + 10, bgCol);
		drawText(screen, msg, x + 1, y + 1, col);
	}
	public static void drawText(Screen screen, String msg, int x, int y, int col) {
		for (int l = 0; l < msg.length(); l++) {
			int ix = chars.indexOf(msg.charAt(l));
			int offset = ix * 8;
			if (ix >= 0) {
				for (int i = 0; i < 64; i++) {
					int sx = offset + (i / 8);
					int sy = i % 8;
					int pixel = fontData.GetPixel(sx, sy);
					if (pixel == 0xffffffff) {
						 screen.setPixel(x + i / 8 + l * 8, y + sy, col);
					}
				}
			}
			
		}
	}
}
