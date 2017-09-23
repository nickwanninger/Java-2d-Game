package io.nickw.game.level;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import io.nickw.game.Coordinate;
import io.nickw.game.GameObject;
import io.nickw.game.dungeon.Dungeon;
import io.nickw.game.gfx.Screen;
import io.nickw.game.tile.Tile;

public class Level {
	public ArrayList<GameObject> objects;
	public int width;
	public int height;
	
	boolean needsSorting = true;
	
	public int[] tiles;

	Dungeon dungeon;
	

	private Comparator<GameObject> objectSorter = new Comparator<GameObject>() {
		public int compare(GameObject e0, GameObject e1) {
			if (e1.position.y < e0.position.y) return +1;
			if (e1.position.y > e0.position.y) return -1;
			return 0;
		}
	};
	
	
	public Level (int width, int height) {
		objects = new ArrayList<GameObject>();
		this.width = width;
		this.height = height;
		dungeon = new Dungeon(width, height);
		dungeon.Generate(System.currentTimeMillis());
		tiles = dungeon.tileData;
	}
	
	public void addObject(GameObject g) {
		needsSorting = true;
		objects.add(g);
	}
	
	public void render(Screen screen) {
		
		int xo = screen.offset.x / Tile.TILE_WIDTH - 2;
		int yo = screen.offset.y / Tile.TILE_WIDTH - 2;
		int w = (screen.width) / Tile.TILE_WIDTH + 4;
		int h = (screen.height) / Tile.TILE_WIDTH + 4;
		for (int x = xo; x < w + xo; x++ ) {
			for (int y = yo; y < h + yo; y++ ) {
				Tile tile = getTile(x,y);
				if (tile != null) {
					getTile(x,y).render(screen,this, x * Tile.TILE_WIDTH, y * Tile.TILE_WIDTH);
				}
				
			}
		}
		Collections.sort(objects, objectSorter);
		for	(int i = 0; i < objects.size(); i++) {
			objects.get(i).render(screen);
		}
	}
	
	
	public Tile getTile(int x, int y) {
		return Tile.tiles[getTileType(x, y)];
	}


	public int getTileType(int x, int y) {
		if (x < 0 || y < 0 || x >= width || y >= height) return 0;
		return tiles[x + y * height];
	}

	public Tile getTileAtPixelPos(int x, int y) {
		return getTile(x / Tile.TILE_WIDTH, y / Tile.TILE_WIDTH);
	}

	public Tile getTile(Coordinate t) {
		return getTile(t.x, t.y);
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


	public boolean isPassable(int x, int y) {
		return getTile(x,y).passable;
	}
}


