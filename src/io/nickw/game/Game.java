package io.nickw.game;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.MouseInfo;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import javax.swing.*;
import io.nickw.game.entity.Player;
import io.nickw.game.gfx.Color;
import io.nickw.game.gfx.Font;
import io.nickw.game.gfx.Screen;
import io.nickw.game.gfx.Sprite;
import io.nickw.game.gfx.SpriteReference;
import io.nickw.game.level.Level;
import io.nickw.game.tile.Tile;

public class Game extends Canvas implements Runnable {

	private static final long serialVersionUID = 1L;
	private static final int WIDTH = 128;
	private static final int HEIGHT = 128;
	private static final int windowWidth = 600;
	private static final int windowHeight = windowWidth * HEIGHT / WIDTH;

	private static final String NAME = "Java Game";
	private JFrame frame;
	private boolean running = false;
	private int tickCount = 0;

	public static float gravity = 0.1f;
	public static float terminalVelocity = 3f;

	private static int mouseX = 0;
	private static int mouseY = 0;

	private Screen screen;
	private InputHandler input;

	public static int fps = 0;
	public static int tps = 0;

	Font font = new Font();
	Level level = new Level(100, 100);

	private BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
	private int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();

	Player player;

	public Game() {
		setMinimumSize(new Dimension(windowWidth, windowHeight));
		setMaximumSize(new Dimension(windowWidth, windowHeight));
		setPreferredSize(new Dimension(windowWidth, windowHeight));
		frame = new JFrame(Game.NAME);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		frame.add(this, BorderLayout.CENTER);
		frame.pack();
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	public void init() {
		screen = new Screen(WIDTH, HEIGHT, new Sprite("/sprite_sheet.png"));
		screen.pixels = pixels;
		input = new InputHandler(this);
		player = new Player(50 * Tile.TILE_WIDTH, 50 * Tile.TILE_WIDTH, level, input);
		level.addObject(player);
	}

	public void start() {
		System.out.println("Started");
		running = true;
		new Thread(this).start();
	}

	public void stop() {
		running = false;
	}

	public void run() {
		long lastTime = System.nanoTime();
		double TICKRATE = 60.0;
		double nsPerTick = 1000000000D / TICKRATE;

		int frames = 0;
		int ticks = 0;

		long lastTimer = System.currentTimeMillis();
		double delta = 0;

		init();

		while (running) {
			long now = System.nanoTime();
			delta += (now - lastTime) / nsPerTick;
			lastTime = now;
			boolean shouldRender = true;

			while (delta >= 1) {
				ticks++;
				tick();
				delta -= 1;
				shouldRender = true;
			}

			if (shouldRender) {
				frames++;
				render();
			}

			double deltaT = System.currentTimeMillis() - lastTimer;

			if (deltaT >= 1000) {
				lastTimer += 1000;
				Game.fps = frames;
				Game.tps = ticks;
				System.out.println(frames + " frames " + ticks + " ticks ");
				frames = 0;
				ticks = 0;
			}
		}
	}

	public void tick() {
		input.tick();
		tickCount++;
		if (!hasFocus()) {
			input.releaseAll();
		} else {
			level.tick();
		}
	}

	public void render() {
		int xo = player.position.x - (WIDTH - Tile.TILE_WIDTH) / 2;
		int yo = player.position.y - ((HEIGHT - Tile.TILE_WIDTH) / 2) + 5;
		screen.setOffset(xo, yo);

		BufferStrategy bs = getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(3);
			requestFocus();
			return;
		}
		// update the mouse location every frame:
		int rawMouseX = MouseInfo.getPointerInfo().getLocation().x - getLocationOnScreen().x;
		int rawMouseY = MouseInfo.getPointerInfo().getLocation().y - getLocationOnScreen().y;
		Game.mouseX = (int) (rawMouseX / (float) windowWidth * WIDTH);
		Game.mouseY = (int) (rawMouseY / (float) windowHeight * HEIGHT);

		screen.clear(0xff00ff);

		level.render(screen);
		drawGUI(screen);
		drawFocusText();
		Graphics g = bs.getDrawGraphics();
		g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
		g.dispose();
		bs.show();
	}

	public void drawGUI(Screen screen) {
		int yo = HEIGHT - 8;
		screen.drawSquare(0, yo - 1, WIDTH, yo, 0x000);

		for (int x = 0; x < WIDTH; x ++) {
			for (int y = 0; y < 16; y ++) {
				int dy = y + yo;
				screen.setPixel(x, dy, 0x000000);
			}
		}

//		Font.drawWithFrame(screen, Game.fps + "fps", 0, yo);
		SpriteReference fullHeart = new SpriteReference(new Coordinate(0, 8 * 6), 4, 4);
		SpriteReference emptyHeart = new SpriteReference(new Coordinate(4, 8 * 6), 4, 4);
		// draw the player's health to the GUI Bar
		for (int i = 0; i < player.maxHealth; i++) {
			int x = 3 + i * 4 + screen.offset.x;
			int y = yo + 9 + screen.offset.y;
			if (i >= player.health) {
				screen.drawSprite(emptyHeart, x, y);
			} else {
				screen.drawSprite(fullHeart, x, y);
			}
		}
	}

	public void drawFocusText() {
		if (!hasFocus()) {
			for (int i = 0; i < WIDTH * HEIGHT; i++) {
				screen.pixels[i] = Color.Adjust(screen.pixels[i], 0.3f);
			}
			String s1 = "Paused";
			int sX = WIDTH / 2 - s1.length() * 6 / 2 - 5;
			int sY = 5;
			screen.drawSquare(sX, sY, sX + s1.length() * 6 + 9, sY + 42, 0xff000000);
			Font.drawText(screen, s1, WIDTH / 2 - s1.length() * 6 / 2, 10, 0x008751);
			if (Math.sin(tickCount / 4) >= 0) {
				int ctfY = 20;
				Font.drawText(screen, "CLICK", WIDTH / 2 - 5 * 6 / 2, ctfY, 0xffffff);
				Font.drawText(screen, "TO", WIDTH / 2 - 2 * 6 / 2, ctfY + 8, 0xffffff);
				Font.drawText(screen, "FOCUS", WIDTH / 2 - 5 * 6 / 2, ctfY + 16, 0xffffff);
			}
		}
	}

	public static void main(String[] args) {
		Game game = new Game();
		game.start();
	}
	
}
