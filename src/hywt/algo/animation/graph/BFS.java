package hywt.algo.animation.graph;

import hywt.algo.datatype.Direction;
import hywt.algo.datatype.Graph;

import java.awt.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;

public class BFS extends BasicGraph {

    protected LinkedList<Point> points = new LinkedList<>();
    protected HashMap<Point, Direction> directions = new HashMap<>();
    protected boolean solved = false;
    protected boolean path = false;

    Point next;

    public BFS() throws IOException {
        super();
        points.add(graph.getStart());
    }

    public BFS(Graph graph) throws IOException {
        this.graph = graph;
        points.add(graph.getStart());
    }


    @Override
    public void provideFrame(Graphics g) {
        super.provideFrame(g);
        String info = String.format("广度优先搜索 - queue length=%d", points.size());
        g.drawChars(info.toCharArray(), 0, info.length(), 4, 12);
        if (tick > 150) {
            if (!solved) {
                search(points.pop());
            } else if (!path) {
                graph.set(next, Graph.PATH);
                Direction dir = directions.get(next);
                if (dir == null) {
                    path = true;
                } else {
                    next = dir.move(next);
                }
            }
        }

    }

    @Override
    public boolean hasNext() {
        return !(solved && path);
    }

    private void search(Point p) {
        if (graph.get(p) == Graph.EMPTY) graph.set(p, Graph.EXPLORED);
        addPoint(new Point(p.x, p.y + 1), Direction.NORTH);
        addPoint(new Point(p.x, p.y - 1), Direction.SOUTH);
        addPoint(new Point(p.x + 1, p.y), Direction.WEST);
        addPoint(new Point(p.x - 1, p.y), Direction.EAST);
    }

    private void addPoint(Point p, Direction direction) {
        if (graph.get(p) == Graph.END) {
            solved = true;
            next = p;
            directions.put(p, direction);
        } else if (graph.get(p) == Graph.EMPTY && !points.contains(p)) {
            points.add(p);
            directions.put(p, direction);
        }
    }
}
