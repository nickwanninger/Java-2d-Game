package io.nickw.game;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import javax.swing.JFrame;
import javax.swing.KeyStroke;

import io.nickw.game.gfx.Screen;
import io.nickw.game.gfx.Sprite;


public class Game extends Canvas implements Runnable {
	
	
	private static final long serialVersionUID = 1L;
	public static final int WIDTH = 160;
	public static final int HEIGHT = 120;
	public static final int SCALE = 3;
	public static final String NAME = "Game (name is a wip)";
	private JFrame frame;
	public boolean running = false;
	public int tickCount = 0;
	
	private Screen screen;
	public InputHandler input;
		
	Level level = new Level(WIDTH, HEIGHT);
	
	public static int fps = 0;
	
	private BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
	private int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
	
	
	public Game() {
		Player a = new Player(0, 0);
		level.addObject(a);
		

		setMinimumSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		setMaximumSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
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
		input = new InputHandler(this);
		addKeyListener(input);
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
			boolean shouldRender = true;
			
			while (delta >= 1) {
				ticks++;
				tick();
				delta -= 1;
				shouldRender = true;
			}
			
			try {
				Thread.sleep(2);
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
				System.out.println(frames + " FPS, " + ticks + " TPS, " + Level.objects.size() +" objects in scene");
				frames = 0;
				ticks = 0;
			}
			
			
		}
	}
	
	
	public void tick() {
		tickCount ++;
		if (!hasFocus()) {
			input.releaseAll();
		} else {
			input.tick();
			level.tick();
		}
		
	}
	
	public void render() {
		
		
		BufferStrategy bs = getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(3);
			requestFocus();
			return;
		}
		screen.clear(0xffCFFAFB);
		level.render(screen);
		
		
		for (int i = 0; i < screen.pixels.length; i++) {
			pixels[i] = screen.pixels[i];
		}
		
		Graphics g = bs.getDrawGraphics();
		g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
		g.dispose();
		bs.show();
		
	}

	public static void main(String[] args) {
		//new Game().start();
		Game game = new Game();

		game.start();
	}

}
