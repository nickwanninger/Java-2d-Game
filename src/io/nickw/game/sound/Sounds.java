package io.nickw.game.sound;

import java.applet.Applet;
import java.applet.AudioClip;
import kuusisto.tinysound.*;

public class Sounds {
	public static final kuusisto.tinysound.Sound hit = TinySound.loadSound("/sound/Hit.wav",true);
	public static final kuusisto.tinysound.Sound jump = TinySound.loadSound("/sound/Jump.wav",true);
	public static final kuusisto.tinysound.Sound land = TinySound.loadSound("/sound/Land.wav",true);
	public static final kuusisto.tinysound.Sound step = TinySound.loadSound("/sound/Step.wav",true);
	public static final kuusisto.tinysound.Sound shoot = TinySound.loadSound("/sound/Shoot.wav",true);
	public static final kuusisto.tinysound.Sound flame = TinySound.loadSound("/sound/Flame.wav",true);
	public static final kuusisto.tinysound.Sound poof = TinySound.loadSound("/sound/Poof.wav",true);
	public static final kuusisto.tinysound.Sound explosion = TinySound.loadSound("/sound/Explosion.wav");
	public static final kuusisto.tinysound.Sound click = TinySound.loadSound("/sound/Click.wav");
	public static final kuusisto.tinysound.Sound select = TinySound.loadSound("/sound/Select.wav");
	public static final kuusisto.tinysound.Sound open = TinySound.loadSound("/sound/OpenMenu.wav");
	public static final kuusisto.tinysound.Sound close = TinySound.loadSound("/sound/CloseMenu.wav");
	public static final kuusisto.tinysound.Sound error = TinySound.loadSound("/sound/Error.wav");


}