package io.nickw.game.dungeon;
import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import javax.swing.*;
public class DungeonTester extends Canvas implements Runnable {

	private static final long serialVersionUID = 1L;
	private static final int WIDTH = 128;
	private static final int HEIGHT = 128;
	private static final int scale = 4;
	public int frameCount = 0;

	private JFrame frame;
	private boolean running = false;
	private int tickCount = 0;

	Dungeon dungeon;


	private BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
	private int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();

	public DungeonTester() {
		setMinimumSize(new Dimension(WIDTH * scale, HEIGHT * scale));
		setMaximumSize(new Dimension(WIDTH * scale, HEIGHT * scale));
		setPreferredSize(new Dimension(WIDTH * scale, HEIGHT * scale));
		frame = new JFrame("Dungeon Tester");
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		frame.add(this, BorderLayout.CENTER);
		frame.pack();
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		dungeon = new Dungeon(WIDTH, HEIGHT);
	}


	public void start() {
		running = true;
		dungeon.Generate(1);
		new Thread(this).start();
	}


	public void run() {
		long lastTime = System.nanoTime();
		double nsPerTick = 1000000000D / 60d;
		int frames = 0;
		int ticks = 0;
		long lastTimer = System.currentTimeMillis();
		double delta = 0;
		render();
		while (running) {
			long now = System.nanoTime();
			delta += (now - lastTime) / nsPerTick;
			lastTime = now;
			while (delta >= 1) {
				delta -= 1;
				render();
			}
		}
	}

	public int getColor(int blockIndex) {

		if (blockIndex == 1) return 0x000000;
		if (blockIndex == 2) return 0xff00ff;
		return 0xffffff;
	}

	public void render() {
		frameCount++;
		BufferStrategy bs = getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(3);
			requestFocus();
			return;
		}
		for (int i = 0; i < pixels.length; i++) {
			pixels[i] = getColor(dungeon.tileData[i]);
		}
		Graphics g = bs.getDrawGraphics();
		g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
		g.dispose();
		bs.show();
	}

	public static void main(String[] args) {
		DungeonTester tester = new DungeonTester();
		tester.start();
	}

}
