package io.nickw.game.util;

public class Cooldown {

	private int time = 0;
	private int maxTime = 0;

	public Cooldown(int t) {
		this.time = t;
		this.maxTime = t;
	}

	public int getTimeLeft() {
		return time;
	}

	public double getProgress() {
		if (maxTime != 0) {
			return time / (double) maxTime;
		} else {
			return 1.0;
		}
	}

	public boolean isDone() {
		return time == 0;
	}

	public void tick() {
		if (time > 0) {
			time--;
		}
	}

	public void reset(int max) {
		this.maxTime = max;
		this.time = max;
	}

	public void reset() {
		reset(this.maxTime);
	}
}