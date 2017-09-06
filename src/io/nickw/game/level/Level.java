package io.nickw.game.level;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import io.nickw.game.GameObject;
import io.nickw.game.gfx.Screen;
import io.nickw.game.tile.Brick;
import io.nickw.game.tile.Tile;

public class Level {
	public static ArrayList<GameObject> objects;
	public int width;
	public int height;
	
	boolean needsSorting = true;
	
	public Tile[] tiles;
	

	private Comparator<GameObject> objectSorter = new Comparator<GameObject>() {
		public int compare(GameObject e0, GameObject e1) {
			if (e1.order < e0.order) return +1;
			if (e1.order > e0.order) return -1;
			return 0;
		}
	};
	
	
	public Level (int width, int height) {
		objects = new ArrayList<GameObject>();
		this.width = width;
		this.height = height;
		tiles = new Tile[width * height];
		for (int i = 0; i < (width * height); i++) {
			int x = (i % width) * 8;
			int y = (i / height) * 8;
			if (x > y - 32) continue;
			tiles[i] = new Brick(x, y, this);
		}
		
	}
	
	public void addObject(GameObject g) {
		needsSorting = true;
		objects.add(g);
	}
	
	public void render(Screen screen) {
		
		int xo = screen.offset.x / 8;
		int yo = screen.offset.y / 8;
		int w = (screen.width + 8) / 8;
		int h = (screen.height + 8) / 8;
		for (int x = xo; x < w + xo; x++ ) {
			for (int y = yo; y < h + yo; y++ ) {
				Tile tile = getTile(x,y);
				if (tile != null) {
					getTile(x,y).render(screen);
				}
				
			}	
		}
		if (needsSorting) {
			Collections.sort(objects, objectSorter);
			needsSorting = false;
		}
		for	(int i = 0; i < objects.size(); i++) {
			objects.get(i).render(screen);
		}
	}
	
	
	public Tile getTile(int x, int y) {
		if (x < 0 || y < 0 || x >= width || y >= height) return new Tile(x * 8,y * 8,this);;
		return tiles[x + y * height];
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


