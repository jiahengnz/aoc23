package main;

import java.util.List;
import java.util.Objects;

public class Vector2 {

    public int x;
    public int y;

    public Vector2(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public static final Vector2 ZERO = new Vector2(0, 0);
    public static final Vector2 ONE = new Vector2(1, 1);

    public static final Vector2 UP = new Vector2(0, -1);
    public static final Vector2 DOWN = new Vector2(0, 1);
    public static final Vector2 LEFT = new Vector2(-1, 0);
    public static final Vector2 RIGHT = new Vector2(1, 0);

    public static final Vector2 NORTH = UP;
    public static final Vector2 EAST = RIGHT;
    public static final Vector2 SOUTH = DOWN;
    public static final Vector2 WEST = LEFT;

    public static List<Vector2> directions = List.of(NORTH, Vector2.EAST, Vector2.SOUTH, Vector2.WEST);

    public Vector2 flip() {
        return new Vector2(-x, -y);
    }

    public boolean isPerpendicularTo(Vector2 other) {
        return this.dot(other) == 0;
    }

    public Vector2 add(Vector2 amount) {
        return new Vector2(this.x + amount.x, this.y + amount.y);
    }

    public Vector2 mult(int scale) {
        return new Vector2(this.x * scale, this.y * scale);
    }

    public Vector2 turnLeft() {
        return new Vector2(y, -x);
    }

    public Vector2 turnRight() {
        return new Vector2(-y, x);
    }

    public boolean isUpDown() {
        return x == 0;
    }

    public boolean isLeftRight() {
        return y == 0;
    }

    public boolean isLeftTurn(Vector2 newDirection) {
        return turnLeft().equals(newDirection);
    }

    public boolean isInBound(int x1, int y1, int x2, int y2) {
        return x >= x1 && y >= y1 && x < x2 && y < y2;
    }

    public boolean isInBound(int maxX, int maxY) {
        return isInBound(0, 0, maxX, maxY);
    }

    public int dot(Vector2 other) {
        return other.x * this.x + other.y * this.y;
    }

    public String name() {
        if (equals(UP)) return "↑";
        if (equals(DOWN)) return "↓";
        if (equals(LEFT)) return "←";
        if (equals(RIGHT)) return "→";
        throw new RuntimeException("Not a unit vector " + this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vector2 vector2 = (Vector2) o;
        return this.x == vector2.x && this.y == vector2.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "Vector(" + x + ", " + y + ')';
    }


    public int taxicabDist(Vector2 goal) {
        return Math.abs(goal.x - x) + Math.abs(goal.y - y);
    }

    public Vector2 modulus(int maxX, int maxY) {
        return new Vector2((x % maxX + maxX) % maxX, (y % maxY + maxY) % maxY);
    }
}
