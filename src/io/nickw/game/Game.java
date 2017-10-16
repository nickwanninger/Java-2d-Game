package io.nickw.game;

import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import javax.imageio.ImageIO;
import javax.swing.*;

import io.nickw.game.entity.Player;
import io.nickw.game.entity.enemy.Enemy;
import io.nickw.game.gfx.*;
import io.nickw.game.gfx.Font;
import io.nickw.game.level.Level;
import io.nickw.game.tile.Tile;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;

import com.sun.management.OperatingSystemMXBean;
import io.nickw.game.util.InputHandler;
import io.nickw.game.util.Mouse;

public class Game extends Canvas implements Runnable {

	private static final long serialVersionUID = 1L;
	public static final int WIDTH = 160;
	public static final int HEIGHT = 120;
	private static final int windowWidth = WIDTH * 6;
	private static final int windowHeight = windowWidth * HEIGHT / WIDTH;

	public ArrayList<Integer> performanceHistory = new ArrayList<>();

	private static final String NAME = "Java Game";
	private GameFrame frame;
	private boolean running = false;
	public static int tickCount = 0;

	public static float gravity = 0.1f;
	public static float terminalVelocity = 3f;

	public static int mouseX = 0;
	public static int mouseY = 0;

	public static String OS = System.getProperty("os.name").toLowerCase();

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


	BufferedImage cursorImg = new BufferedImage(8, 8, BufferedImage.TYPE_INT_ARGB);
	Cursor cursor = null;


	public Game() {

		setMinimumSize(new Dimension(windowWidth, windowHeight));
		setMaximumSize(new Dimension(windowWidth, windowHeight));
		setPreferredSize(new Dimension(windowWidth, windowHeight));
		frame = new GameFrame(Game.NAME);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		frame.add(this, BorderLayout.CENTER);
		frame.pack();
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		addMouseListener(mouse);

		JPanel keyPanel = new JPanel();

		frame.add(keyPanel, BorderLayout.CENTER);
		input = new InputHandler(keyPanel);

		cursor = Toolkit.getDefaultToolkit().createCustomCursor(cursorImg, new Point(0, 0), "blank cursor");

	}


	public void init() {
		lightingEngine = new LightingEngine(WIDTH, HEIGHT, level);
		screen = new Screen(WIDTH, HEIGHT, new Sprite("/sprite_sheet.png"));
		screen.pixels = pixels;
		lightScreen = new Screen(WIDTH, HEIGHT, new Sprite("/sprite_sheet.png"));

		player = new Player(50 * Tile.TILE_WIDTH, 50 * Tile.TILE_WIDTH, level, input);
		level.addObject(player);
		for (int i = 0; i < 10; i++) {
			Enemy e = new Enemy(50 * Tile.TILE_WIDTH, 50 * Tile.TILE_WIDTH, level);
			level.addObject(e);
		}

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

			try {
				Thread.sleep(2);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			double deltaT = System.currentTimeMillis() - lastTimer;

			if (deltaT >= 1000) {
				lastTimer += 1000;
				Game.fps = frames;
				Game.tps = ticks;
				frames = 0;
				ticks = 0;
			}
		}
	}

	public void tick() {

		tickCount++;
		if (!hasFocus()) {
			input.releaseAll();
		} else {
			level.tick();
		}
		input.tick();

	}


	public void render() {
		if (hasFocus()) {
//			setCursor(cursor);
			int rawMouseX = MouseInfo.getPointerInfo().getLocation().x - getLocationOnScreen().x;
			int rawMouseY = MouseInfo.getPointerInfo().getLocation().y - getLocationOnScreen().y;
			Game.mouseX = (int) (rawMouseX / (float) windowWidth * WIDTH);
			Game.mouseY = (int) (rawMouseY / (float) windowHeight * HEIGHT);
		}
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
		lightingEngine.overlayOntoScreen(screen);


		drawGUI(screen);
		if (input.debug.down) {
			drawDebug(screen);
		}


		player.drawMenus(screen);

//		screen.drawSpriteGUI(new SpriteReference(0, 104, 8, 8), Game.mouseX, Game.mouseY);

		Graphics g = bs.getDrawGraphics();
		g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
		g.dispose();
		bs.show();
	}

	public void drawGUI(Screen screen) {

		screen.drawSpriteGUI(new SpriteReference(200, 0, 46, 12), 0, 0);
		SpriteReference s_mana = new SpriteReference(new Coordinate(193, 0), 1, 4);
		SpriteReference s_health = new SpriteReference(new Coordinate(192, 0), 1, 4);

		int drawCount = 33;

		double healthPercentage = player.health / (double) player.maxHealth;
		double manaPercentage = player.mana / (double) player.maxMana;

		int healthDrawCount = (int) (healthPercentage * drawCount);
		int manaDrawCount = (int) (manaPercentage * drawCount);

		for (int i = 0; i < healthDrawCount; i++) {
			screen.drawSpriteGUI(s_health, 11 + i, 2);
		}

		for (int i = 0; i < manaDrawCount; i++) {
			screen.drawSpriteGUI(s_mana, 11 + i, 6);
		}

	}


	public void drawDebug(Screen screen) {

		String[] lines = new String[] {
				Game.fps + "fps",
				Game.tps + "tps",
				tickCount + " ticks",
				level.objects.size() + " objects",
				"mouse: " + (Mouse.left.down ? "clicked" : "released"),
				"Thread Count: " + Thread.activeCount()
		};

		for (int i = 0; i < lines.length; i++) {
			Font.drawText(screen, lines[i], 1, 1 + 7 * i, 0xffffff);
		}

	}

	public static void main(String[] args) {
		Game game = new Game();
		game.start();
	}

}