package io.nickw.game;

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.Random;
import javax.swing.*;
import io.nickw.game.entity.Player;
import io.nickw.game.entity.Slime;
import io.nickw.game.gfx.*;
import io.nickw.game.gfx.Color;
import io.nickw.game.gfx.Font;
import io.nickw.game.level.Level;
import io.nickw.game.tile.Tile;
import java.lang.management.ManagementFactory;
import com.sun.management.OperatingSystemMXBean;

public class Game extends Canvas implements Runnable {

	private static final long serialVersionUID = 1L;
	private static final int WIDTH = 180;
	private static final int HEIGHT = 120;
	private static final int windowWidth = 1000;
	private static final int windowHeight = windowWidth * HEIGHT / WIDTH;

	private static final String NAME = "Java Game";
	private JFrame frame;
	private boolean running = false;
	public static int tickCount = 0;

	public static float gravity = 0.1f;
	public static float terminalVelocity = 3f;

	public static int mouseX = 0;
	public static int mouseY = 0;

	private Screen screen;
	private Screen lightScreen;
	private InputHandler input;
	public static Mouse mouse = new Mouse();

	public static int fps = 0;
	public static int tps = 0;

	Font font = new Font();
	Level level = new Level(100, 100);

	private BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
	private int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
	LightingEngine lightingEngine;
	Player player;
	OperatingSystemMXBean operatingSystemMXBean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();

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
		addMouseListener(mouse);
	}

	public void init() {
		lightingEngine = new LightingEngine(WIDTH, HEIGHT, level);
		screen = new Screen(WIDTH, HEIGHT, new Sprite("/sprite_sheet.png"));
		screen.pixels = pixels;
		lightScreen = new Screen(WIDTH, HEIGHT, new Sprite("/sprite_sheet.png"));
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
			while (delta >= 1) {
				ticks++;
				tick();
				delta -= 1;
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

		if (hasFocus()) {
			int rawMouseX = MouseInfo.getPointerInfo().getLocation().x - getLocationOnScreen().x;
			int rawMouseY = MouseInfo.getPointerInfo().getLocation().y - getLocationOnScreen().y;
			Game.mouseX = (int) (rawMouseX / (float) windowWidth * WIDTH);
			Game.mouseY = (int) (rawMouseY / (float) windowHeight * HEIGHT);
		}

		input.tick();
		tickCount++;
		if (!hasFocus()) {
			input.releaseAll();
		} else {
			level.tick();
		}
	}


	public void render() {
		int xo = (int) player.position.x - (WIDTH - Tile.TILE_WIDTH) / 2;
		int yo = (int) player.position.y - (HEIGHT - Tile.TILE_WIDTH) / 2  ;

		xo += (player.velocity.x * 5);
		yo += (player.velocity.y * 5);

		lightingEngine.setOffset(xo, yo);
		screen.setOffset(xo, yo);
		screen.clear(0);

		BufferStrategy bs = getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(3);
			requestFocus();
			return;
		}


		level.render(screen);
		// draw light screen over top the normal screen
		lightingEngine.overlayOntoScreen(screen);

//		for (int i = 0; i < screen.pixels.length; i++) {
//			float d = (float) Math.max(Math.random() - 0.7f, 0f);
//			screen.pixels[i] = Color.desaturate(screen.pixels[i], 0.9f);
//		}


		drawGUI(screen);
		if (input.debug.down) {
			drawDebug(screen);
		}



		Graphics g = bs.getDrawGraphics();
		g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
		g.dispose();
		bs.show();
	}

	public void drawGUI(Screen screen) {
		SpriteReference s_mana = new SpriteReference(new Coordinate(4, 112), 4, 4);
		SpriteReference s_health = new SpriteReference(new Coordinate(0, 112), 4, 4);
		SpriteReference s_empty = new SpriteReference(new Coordinate(0, 116), 4, 4);

		for (int i = 0; i < player.mana; i++) {
			SpriteReference toDraw = i < player.maxMana ? s_mana : s_empty;
			screen.drawSpriteGUI(toDraw, 1 + 4 * i, 1);
		}

		for (int i = 0; i < player.health; i++) {
			SpriteReference toDraw = i <= player.maxHealth ? s_health : s_empty;
			screen.drawSpriteGUI(toDraw, 1 + 4 * i, 6);
		}

	}

	public void drawDebug(Screen screen) {
		String[] lines = new String[] {
				Game.fps + "fps",
				Game.tps + "tps",
				tickCount + " ticks",
				level.objects.size() + " objects",
				"mouse: " + (Mouse.left.down ? "clicked" : "released"),
				"CPU: " + Math.round(operatingSystemMXBean.getProcessCpuLoad() * 100) + "%"
		};

		for (int i = 0; i < lines.length; i++) {
			Font.drawText(screen, lines[i], 1, 1 + 7 * i, 0xffffff);
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
			screen.drawSquare(sX, sY, sX + s1.length() * 6 + 9, sY + 42, 0x000000);
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
