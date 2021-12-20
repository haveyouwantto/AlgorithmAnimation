package hywt.algo.datatype;

import hywt.algo.animation.graph.BFS;

import java.awt.*;

public class Direction {
    public static final Direction NORTH = new Direction(0, -1);
    public static final Direction SOUTH = new Direction(0, 1);
    public static final Direction WEST = new Direction(-1, 0);
    public static final Direction EAST = new Direction(1, 0);

    private final int x;
    private final int y;

    public Direction(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Point move(Point origin) {
        return new Point(origin.x + x, origin.y + y);
    }
}
