package io.nickw.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import io.nickw.game.gfx.Screen;

public class Level {
	public static ArrayList<GameObject> objects;
	public static int width;
	public static int height;
	

	private Comparator<GameObject> objectSorter = new Comparator<GameObject>() {
		public int compare(GameObject e0, GameObject e1) {
			if (e1.order < e0.order) return +1;
			if (e1.order > e0.order) return -1;
			return 0;
		}
	};
	
	
	public Level (int width, int height) {
		objects = new ArrayList<GameObject>();
		Level.width = width;
		Level.height = height;
	}
	
	public void addObject(GameObject g) {
		objects.add(g);
	}
	
	public void render(Screen screen) {
		Collections.sort(objects, objectSorter);
		for	(int i = 0; i < objects.size(); i++) {
			objects.get(i).render(screen);
		}
	}
	
	
	
	public void remove(GameObject g) {
		for	(int i = 0; i < objects.size(); i++) {
			if (g == objects.get(i)) {
				objects.remove(i);
				return;
			}
		}
	}
	
	public void tick() {
		for	(int i = 0; i < objects.size(); i++) {
			objects.get(i).tick();
		}
	}
}


