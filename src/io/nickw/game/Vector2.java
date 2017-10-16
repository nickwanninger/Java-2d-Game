package io.nickw.game;

public class Vector2 {

	public static final Vector2 zero = new Vector2(0f, 0f);
	public static final Vector2 one = new Vector2(1f, 1f);
	public static final Vector2 up = new Vector2(0f, 1f);
	public static final Vector2 down = new Vector2(0f, -1f);
	public static final Vector2 left = new Vector2(-1f, 0f);
	public static final Vector2 right = new Vector2(1f, 0f);
	public static final Vector2 positiveInfinityVector = new Vector2(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY);
	public static final Vector2 negativeInfinityVector = new Vector2(Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY);

	public float x, y;
	public Vector2 (float x, float y) {
		this.x = x;
		this.y = y;
	}
	public Vector2() {
		this(0,0);
	}

	public void divideScalar(float r) {
		if (r > 0) {
			x /= r;
			y /= r;
		}
	}

	public void multiplyScalar(float r) {
		x *= r;
		y *= r;
	}

	public void add(Vector2 a) {
		this.x += a.x;
		this.y += a.y;
	}


	public void zeroOut() {
		multiplyScalar(0);
	}
	public static float getMagnitude(Vector2 a) {
		return (float) Math.sqrt(a.x * a.x + a.y * a.y);
	}

	public static Vector2 normalize(Vector2 a) {
		return Vector2.divide(a, Vector2.getMagnitude(a));
	}


	public static Vector2 add(Vector2 a, Vector2 b) {
		return new Vector2(a.x + b.x, a.y + b.y);
	}


	public static Vector2 subtract(Vector2 a, Vector2 b) {
		return new Vector2(a.x - b.x, a.y - b.y);
	}


	public static Vector2 multiply(Vector2 a, Vector2 b) {
		return new Vector2(a.x * b.x, a.y * b.y);
	}


	public static Vector2 multiply(Vector2 a, float b) {
		return new Vector2(a.x * b, a.y * b);
	}


	public static Vector2 divide(Vector2 a, Vector2 b) {
		if (b.x == 0 || b.y == 0) return a;
		return new Vector2(a.x / b.x, a.y / b.y);
	}

	public static Vector2 square(Vector2 a) {
		return Vector2.multiply(a, a);
	}

	public static Vector2 divide(Vector2 a, float b) {
		if (b == 0) return a;
		return new Vector2(a.x / b, a.y / b);
	}


	public static Vector2 clampMagnitude(Vector2 vector, float maxLength)
	{
		Vector2 result;
		if (Math.pow(Vector2.getMagnitude(vector), 2) > maxLength * maxLength)
		{
			result = Vector2.multiply(Vector2.normalize(vector), maxLength);
		}
		else
		{
			result = vector;
		}
		return result;
	}

	public Coordinate toCoordinate() {
		return new Coordinate(Math.round(x), Math.round(y));
	}

	public String toString() {
		return "(" + x + ", " + y + ")";
	}


}
