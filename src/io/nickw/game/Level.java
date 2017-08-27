package io.nickw.game;

import java.util.ArrayList;

import io.nickw.game.gfx.Screen;

public class Level {
	public static ArrayList<GameObject> objects;
	public static int width;
	public static int height;
	public Level (int width, int height) {
		objects = new ArrayList<GameObject>();
		Level.width = width;
		Level.height = height;
	}
	
	public void addObject(GameObject g) {
		objects.add(g);
	}
	
	public void render(Screen screen) {
		for	(int i = 0; i < objects.size(); i++) {
			objects.get(i).render(screen);
		}
	}
	
	public void tick() {
		for	(int i = 0; i < objects.size(); i++) {
			objects.get(i).tick();
		}
	}
}
