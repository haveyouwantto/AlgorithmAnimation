package hywt.algo.animation.graph;

import hywt.algo.datatype.Direction;
import hywt.algo.datatype.Graph;
import hywt.math.function.Function;
import hywt.math.function.Mapper;

import java.awt.*;
import java.io.IOException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;

public class BestFS extends BasicGraph {

    protected PriorityQueue<Point> points;
    protected HashMap<Point, Direction> directions = new HashMap<>();
    protected boolean solved = false;
    protected boolean path = false;
    protected Point end;
    protected Function mapper;

    Point next;

    public BestFS() throws IOException {
        super();
        end = graph.getEnd();
        mapper = new Mapper(0, graph.getHeight() + graph.getWidth(), 2 / 3d, 0);
        Comparator<Point> comparator = Comparator.comparingInt(this::getPriority);
        points = new PriorityQueue<>(comparator);
        points.add(graph.getStart());

        explored = new Color(255, 0, 255);
        registerColorMapper(Graph.PATH, point -> explored);
        registerColorMapper(Graph.EXPLORED, point -> Color.getHSBColor((float) mapper.get(Math.abs(end.x - point.x) + Math.abs(end.y - point.y)), 1, 1));
    }

    protected int getPriority(Point p){
        return Math.abs(end.x - p.x) + Math.abs(end.y - p.y);
    }

    @Override
    public void provideFrame(Graphics g) {
        super.provideFrame(g);
        String info = String.format("最佳优先搜索 - queue length=%d", points.size());
        g.drawChars(info.toCharArray(), 0, info.length(), 4, 12);
        if (tick > 150) {
            if (!solved) {
                search(points.poll());
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
