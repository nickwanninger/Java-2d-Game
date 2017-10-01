package io.nickw.game.dungeon;

import java.util.Random;

public class Dungeon {

	public int width;
	public int height;
	public double seed;
	Random rng;
	public int[] tileData;
	public Room[] rooms;

	public int spawn_x = 0;
	public int spawn_y = 0;

	public Dungeon(int w, int h, double s) {
		width = w;
		height = h;
		seed = s;
		Generate((long) seed);
	}

	int floorVal = 1;
	int wallVal = 2;

	public Dungeon(int w, int h) {
		this(w, h, System.currentTimeMillis());
	}

	public void Generate(long seed) {
		rng = new Random(seed);
		int roomCount = width;
		rooms = new Room[roomCount];
		for (int i = 0; i < roomCount; i++) {
			int w = rng.nextInt(8) + 8;
			int h = rng.nextInt(8) + 8;
			int x = (int) (rng.nextInt(width / 3) + width / 3);
			int y = (int) (rng.nextInt(height / 3) + height / 3);
			rooms[i] = new Room(x, y, w, h);
		}
		tileData = new int[width * height];
		// initialize the tileData into 2 (walls)
		for (int i = 0; i < tileData.length; i++) {
			tileData[i] = wallVal;
		}

		boolean continueLoop;
		do {
			continueLoop = false;
			for (int q = 0; q < rooms.length; q++) {
				for (int j = q + 1; j < rooms.length; j++) {
					Room r1 = rooms[q];
					Room r2 = rooms[j];
					int gap = 1;
					//Check room intersection on both axes
					int xCollide = rangeIntersect(r1.x1, r1.x2 + gap, r2.x1, r2.x2 + gap);
					int yCollide = rangeIntersect(r1.y1, r1.y2 + gap, r2.y1, r2.y2 + gap);
					if (xCollide != 0 && yCollide != 0) {
						continueLoop = true;
						//Determine the smallest movement it would take for the rooms to no longer intersect
						//If the distance moved for x would be shorter
						if (Math.abs(xCollide) < Math.abs(yCollide)) {
							//Get distance to shift both rooms by splitting the returned collision amount in half
							int shift1 = (int) Math.floor(xCollide * 0.5);
							int shift2 = -1 * (xCollide - shift1);
							//Add shift amounts to both room's location data
							r1.x1 += shift1;
							r1.x2 += shift1;
							r2.x1 += shift2;
							r2.x2 += shift2;
							//Else, the distance moved for y would be shorter or the same
						} else {
							//Get distance to shift both rooms by splitting the returned collision amount in half
							int shift1 = (int) Math.floor(yCollide * 0.5);
							int shift2 = -1 * (yCollide - shift1);
							//Add shift amounts to both room's location data
							r1.y1 += shift1;
							r1.y2 += shift1;
							r2.y1 += shift2;
							r2.y2 += shift2;
						}
					}
				}
			}

		} while (continueLoop);

		drawRooms();
		generateCorridors();




	}

	public int getTile(int x, int y) {
		if (x < width && x >= 0 && y < height && y >= 0) {
			return tileData[x + y * height];
		}
		return 0;
	}

	public void setTile(int x, int y, int v) {
		if (x < width - 1 && x > 0 && y < height - 1 && y > 0) {
			tileData[x + y * height] = v;
		}
	}

	int rangeIntersect(int low1, int high1, int low2, int high2) {
		int min1 = Math.min(low1, high1);
		int max1 = Math.max(low1, high1);
		int min2 = Math.min(low2, high2);
		int max2 = Math.max(low2, high2);
		//if the ranges intersect
		if ((max1 >= min2) && (min1 <= max2)) {
			//calculate by how much the ranges intersect
			int dist1 = max2 - min1;
			int dist2 = max1 - min2;
			//if dist2 is smaller
			if (dist2 < dist1) {
				//that means range 0 must be shifted down to no longer intersect
				return dist2 * -1;
				//else, dist2 is larger or equal to dist1
			} else {
				//that means range 0 must be shifted up to no longer intersect
				return dist1;
			}
		} else {
			return 0;
		}
	}

	public int DistanceBetweenRooms(Room r1, Room r2) {
		int x1 = (r1.x1 + r1.x2) / 2;
		int y1 = (r1.y1 + r1.y2) / 2;
		int x2 = (r2.x1 + r2.x2) / 2;
		int y2 = (r2.y1 + r2.y2) / 2;
		return (int) Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
	}

	void generateCorridors() {
		for (int q = 0; q < rooms.length; q++) {
			for (int j = q + 1; j < rooms.length; j++) {
				if (q != j) {
					if (DistanceBetweenRooms(rooms[q], rooms[j]) < 18) {
						BuildPath(rooms[q], rooms[j]);
					}
				}
			}
		}

	}

	public void BuildPath(Room r1, Room r2) {
		int x1 = (r1.x1 + r1.x2) / 2;
		int y1 = (r1.y1 + r1.y2) / 2;
		int x2 = (r2.x1 + r2.x2) / 2;
		int y2 = (r2.y1 + r2.y2) / 2;

		for (int x = Math.min(x1, x2); x < Math.max(x1, x2); x++) {
			setTile(x, y1, floorVal);
		}

		for (int y = Math.min(y1, y2); y < Math.max(y1, y2); y++) {
			setTile(x1, y, floorVal);
		}
	}

	void drawRooms() {
		// draw the rooms to the tileSheet
		for (Room room : rooms) {
			for (int x = room.x1; x < room.x2; x++) {
				for (int y = room.y1; y < room.y2; y++) {
					if (x > 0 && x < width - 1 && y > 0 && y < height - 1) {
						setTile(x,y, floorVal);
					}
				}
			}
		}
	}

}

