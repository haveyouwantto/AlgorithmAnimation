package hywt.algo.animation.graph;

import hywt.algo.datatype.Direction;
import hywt.algo.datatype.Graph;

import java.awt.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;

public class DFS extends BasicGraph {
    protected HashMap<Point, Direction> directions = new HashMap<>();
    protected boolean solved = false;
    protected boolean path = false;
    int depth = 0;

    final Object lock = new Object();
    Thread thread;

    public DFS() throws IOException {
        super();
    }


    @Override
    public void provideFrame(Graphics g) {
        super.provideFrame(g);
        String info = String.format("深度优先搜索 - depth=%d", depth);
        g.drawChars(info.toCharArray(), 0, info.length(), 4, 12);
        if (tick == 150) {
            thread = new Thread(() -> {
                try {
                    search(graph.getStart());
                    drawPath();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
            thread.start();
        } else if (tick > 60) {
            synchronized (lock) {
                lock.notify();
            }
        }
    }

    Point next;

    private void drawPath() throws InterruptedException {
        synchronized (lock) {
            while (true) {
                graph.set(next, Graph.PATH);
                lock.wait();
                Direction dir = directions.get(next);
                if (dir == null) {
                    path = true;
                    break;
                } else {
                    next = dir.move(next);
                }
            }
        }
    }

    private void search(Point point) throws InterruptedException {
        if (solved) return;
        depth++;
        synchronized (lock) {
            if (graph.get(point) == Graph.EMPTY) graph.set(point, Graph.EXPLORED);
            lock.wait();
            LinkedList<Point> paths = getPaths(point);
            for (Point path : paths) {
                if (graph.get(path) == Graph.EMPTY)
                    search(path);
            }
            if(!solved) {
                graph.set(point, Graph.TRIED);
                lock.wait();
            }
        }
        depth--;
    }

    private LinkedList<Point> getPaths(Point p) {
        LinkedList<Point> paths = new LinkedList<>();
        addPoint(paths, new Point(p.x, p.y + 1), Direction.NORTH);
        addPoint(paths, new Point(p.x + 1, p.y), Direction.WEST);
        addPoint(paths, new Point(p.x, p.y - 1), Direction.SOUTH);
        addPoint(paths, new Point(p.x - 1, p.y), Direction.EAST);
        return paths;
    }

    private void addPoint(LinkedList<Point> paths, Point p, Direction direction) {
        if (graph.get(p) == Graph.END) {
            solved = true;
            next = p;
            directions.put(p, direction);
        } else if (graph.get(p) == Graph.EMPTY) {
            paths.add(p);
            directions.put(p, direction);
        }
    }

    @Override
    public boolean hasNext() {
        return !(solved && path);
    }
}
