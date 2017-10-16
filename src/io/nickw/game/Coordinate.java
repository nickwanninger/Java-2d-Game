package io.nickw.game;

public class Coordinate {
	public int x;
	public int y;
	public Coordinate(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public Vector2 toVector2() {
		return new Vector2(x, y);
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}
	
	public String toString() {
		return "(" + x + ", " + y + ")";
	}
}
