package hywt.algo.animation.graph;

import hywt.algo.datatype.Direction;
import hywt.algo.datatype.Graph;
import hywt.algo.datatype.OrderedPriorityQueue;
import hywt.math.function.Function;
import hywt.math.function.Mapper;

import java.awt.*;
import java.io.IOException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.PriorityQueue;

public class Astar extends BasicGraph {

    protected OrderedPriorityQueue<Point> points;
    protected LinkedList<Point> toSearch;
    protected HashMap<Point, Direction> directions = new HashMap<>();
    protected HashMap<Point, Integer> steps = new HashMap<>();
    protected boolean solved = false;
    protected boolean path = false;
    protected Point end;
    protected Function mapper;

    Point next;
    Point current;

    public Astar() throws IOException {
        super();
        end = graph.getEnd();
        mapper = new Mapper(0, 100, 0, 2 / 3d);
        Comparator<Point> comparator = Comparator.comparingInt(this::getPriority);
        points = new OrderedPriorityQueue<>(comparator);
        points.add(graph.getStart());
        toSearch = new LinkedList<>();
        steps.put(graph.getStart(), 0);


        explored = new Color(255, 0, 255);
        registerColorMapper(Graph.PATH, point -> explored);
        registerColorMapper(Graph.EXPLORED, point -> Color.getHSBColor((float) mapper.get(getPriority(point)), 0.5f, 1));
    }

    protected int getDistance(Point p1, Point p2) {
        return Math.abs(p1.x - p2.x) + Math.abs(p1.y - p2.y);
    }

    protected int getPriority(Point p) {
        return steps.get(p) + getDistance(end, p);
    }

    @Override
    public void provideFrame(Graphics g) {
        super.provideFrame(g);
        String info = String.format("A*搜索 - queue length=%d", points.size());
        g.drawChars(info.toCharArray(), 0, info.length(), 4, 12);
        if (tick > 150) {
            if (!solved) {
//                if(toSearch.size()==0){
//                    points.fetch(toSearch);
//                }
                search(points.poll());
                //System.out.println(points);
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

//        int offsetX = 320 - (graph.getWidth() * mul / 2);
//        int offsetY = 240 - (graph.getHeight() * mul / 2);
//        steps.forEach((p, integer) -> {
//            g.setColor(Color.WHITE);
//            String w = String.format("%d", getPriority(p));
//            g.drawChars(w.toCharArray(), 0, w.length(), p.x * mul + offsetX, p.y * mul + offsetY + 14);
//        });
    }

    @Override
    public boolean hasNext() {
        return !(solved && path);
    }

    private void search(Point p) {
        if (graph.get(p) == Graph.EMPTY) graph.set(p, Graph.EXPLORED);
        addPoint(p, new Point(p.x, p.y + 1), Direction.NORTH);
        addPoint(p, new Point(p.x, p.y - 1), Direction.SOUTH);
        addPoint(p, new Point(p.x + 1, p.y), Direction.WEST);
        addPoint(p, new Point(p.x - 1, p.y), Direction.EAST);
    }

    private void addPoint(Point parent, Point p, Direction direction) {
        if (graph.get(p) == Graph.END) {
            solved = true;
            next = p;
            directions.put(p, direction);
        }
        else if (graph.get(p) == Graph.EMPTY) {
            if (!points.contains(p)) {
                steps.put(p, steps.get(parent) + 1);
                points.add(p);
                directions.put(p, direction);
            }
        }

    }
}
