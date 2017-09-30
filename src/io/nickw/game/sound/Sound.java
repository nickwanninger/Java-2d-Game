package io.nickw.game.sound;

import java.applet.Applet;
import java.applet.AudioClip;

public class Sound {
	public static final Sound hit = new Sound("/sound/Hit.wav");
	public static final Sound jump = new Sound("/sound/Jump.wav");
	public static final Sound land = new Sound("/sound/Land.wav");
	public static final Sound step = new Sound("/sound/Step.wav");
	public static final Sound shoot = new Sound("/sound/Shoot.wav");
	
	private AudioClip clip;

	private Sound(String name) {
		try {
			clip = Applet.newAudioClip(Sound.class.getResource(name));
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	public void play() {
		try {
			new Thread(() -> {
				clip.play();
			}).start();
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
}