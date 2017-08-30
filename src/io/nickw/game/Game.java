package io.nickw.game;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.MouseInfo;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import javax.swing.JFrame;
import io.nickw.game.gfx.Color;
import io.nickw.game.gfx.Font;
import io.nickw.game.gfx.Screen;
import io.nickw.game.gfx.Sprite;

public class Game extends Canvas implements Runnable {

	private static final long serialVersionUID = 1L;
	public static final int WIDTH = 300;
	public static final int HEIGHT = WIDTH * 9 / 12;
	public static final int WINDOWSIZE = 800;
	public static final String NAME = "Java Game";
	private JFrame frame;
	public boolean running = false;
	public int tickCount = 0;

	public static float gravity = 0.1f;

	public static int mouseX = 0;
	public static int mouseY = 0;

	private Screen screen;
	public InputHandler input;

	Level level = new Level(16 * 16, 16 * 16);

	public static int fps = 0;
	public static int tps = 0;

	Font font = new Font();

	private BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
	private int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();

	Player player = new Player(Level.width / 2 - 8, Level.height / 2 - 8, level);

	public Game() {
		level.addObject(player);
		int windowWidth = WINDOWSIZE;
		int windowHeight = WINDOWSIZE * 9 / 12;
		setMinimumSize(new Dimension(windowWidth, windowHeight));
		setMaximumSize(new Dimension(windowWidth, windowHeight));
		setPreferredSize(new Dimension(windowWidth, windowHeight));
		frame = new JFrame(Game.NAME);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
		double nsPerTick = 1000000000D / 60D;

		int frames = 0;
		int ticks = 0;

		long lastTimer = System.currentTimeMillis();
		double delta = 0;

		init();

		while (running) {
			long now = System.nanoTime();
			delta += (now - lastTime) / nsPerTick;
			lastTime = now;
			boolean shouldRender = false;

			while (delta >= 1) {
				ticks++;
				tick();
				delta -= 1;
				shouldRender = true;
			}

			try {
				Thread.sleep(0);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (shouldRender) {
				frames++;
				render();
			}

			if (System.currentTimeMillis() - lastTimer >= 1000) {
				lastTimer += 1000;
				Game.fps = frames;
				Game.tps = ticks;
				System.out.println(frames + " FPS, " + ticks + " TPS, " + Level.objects.size() + " objects in scene");
				frames = 0;
				ticks = 0;
			}
		}
	}

	public void tick() {

		tickCount++;
		if (!hasFocus()) {
		} else {
			level.tick();
		}

	}

	public void render() {

		screen.setOffset(player.position.x - (WIDTH - 16) / 2, player.position.y - ((HEIGHT - 16) / 2));

		BufferStrategy bs = getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(3);
			requestFocus();
			return;
		}

		// update the mouse location every frame:
		int rawMouseX = MouseInfo.getPointerInfo().getLocation().x - getLocationOnScreen().x;
		int rawMouseY = MouseInfo.getPointerInfo().getLocation().y - getLocationOnScreen().y;
		int wW = WINDOWSIZE;
		int wH = WINDOWSIZE * 9 / 12;
		Game.mouseX = (int) (rawMouseX / (float) wW * WIDTH);
		Game.mouseY = (int) (rawMouseY / (float) wH * HEIGHT);

		screen.clear(0xffffffff);

		screen.drawSquare(0 - screen.offset.x - 1, 0 - screen.offset.y - 1, Level.width - screen.offset.x + 1,
				Level.height - screen.offset.y + 1, 0xffaaaaaa);
		screen.drawSquare(0 - screen.offset.x, 0 - screen.offset.y, Level.width - screen.offset.x,
				Level.height - screen.offset.y, 0xffffffff);

		level.render(screen);
		Font.drawWithFrame(screen, Game.fps + "fps", 3, 3);
		drawFocusText();
		Graphics g = bs.getDrawGraphics();
		g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
		g.dispose();
		bs.show();
	}

	public void drawFocusText() {
		if (!hasFocus()) {
			for (int i = 0; i < WIDTH * HEIGHT; i++) {
				screen.pixels[i] = Color.Adjust(screen.pixels[i], 0.1f);
			}
			String s1 = "Paused";
			String s2 = "Click to Unpause";
			Font.drawText(screen, s1, WIDTH / 2 - s1.length() * 8 / 2, 10, 0xaaaaaa);
			if (Math.sin(tickCount / 5) >= 0) {
				Font.drawText(screen, s2, WIDTH / 2 - s2.length() * 8 / 2, 28, 0xffffff);
			}
		}
	}

	public static void main(String[] args) {
		Game game = new Game();
		game.start();
	}

}
