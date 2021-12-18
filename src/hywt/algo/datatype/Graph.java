package hywt.algo.datatype;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Graph {
    private int[][] grid;
    private Point start;
    private Point end;

    public static final int EMPTY = 0;
    public static final int WALL = 1;
    public static final int START = 2;
    public static final int END = 3;
    public static final int EXPLORED = -1;
    public static final int PATH = -2;

    public Graph(int width, int height) {
        grid = new int[height][width];
    }

    private Graph(int[][] grid) {
        this.grid = grid;
    }

    public void setStart(int x, int y) {
        start = new Point(x, y);
        set(x, y, START);
    }

    public void setEnd(int x, int y) {
        end = new Point(x, y);
        set(x, y, END);
    }

    public Point getStart() {
        return start;
    }

    public void drawHorizontal(int startX, int endX, int y, int state) {
        for (int i = startX; i <= endX; i++) {
            set(i, y, state);
        }
    }

    public void drawVertical(int x, int startY, int endY, int state) {
        for (int i = startY; i <= endY; i++) {
            set(x, i, state);
        }
    }

    public Point getEnd() {
        return end;
    }

    public void set(int x, int y, int state) {
        grid[y][x] = state;
    }

    public int get(int x, int y) {
        return grid[y][x];
    }

    public void set(Point p, int state) {
        set(p.x, p.y, state);
    }

    public int get(Point p) {
        return get(p.x, p.y);
    }

    public int getWidth() {
        return grid[0].length;
    }

    public int getHeight() {
        return grid.length;
    }

    public static Graph loadImage(File image) throws IOException {
        BufferedImage bim = ImageIO.read(image);
        int width = bim.getWidth(), height = bim.getHeight();
        Graph graph = new Graph(width, height);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                switch (bim.getRGB(x, y) & 0xffffff) {
                    case 0:
                        graph.set(x, y, EMPTY);
                        break;
                    case 0xffffff:
                        graph.set(x, y, WALL);
                        break;
                    case 0xff0000:
                        graph.setStart(x, y);
                        break;
                    case 0xff00:
                        graph.setEnd(x, y);
                        break;
                }
            }
        }
        return graph;
    }

    @Override
    public Object clone() {
        Graph graph = new Graph(grid.clone());
        graph.setStart(start.x, start.y);
        graph.setEnd(end.x, end.y);
        return graph;
    }
}
