package io.nickw.game.util;

public class Timeout {
	public Timeout(Runnable r, long ms) {
		new Thread(() -> {
			try {
				Thread.sleep((long) (ms * (1000 / 60.0)));
				r.run();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}).start();
	}
}