package hywt.algo.animation.scatter;

import hywt.math.shapes.Line2D;
import hywt.math.shapes.Point2D;
import hywt.math.shapes.Vector2D;

import java.awt.*;
import java.util.*;
import java.util.List;

public class KMeans extends BasicScatter {

    List<Point2D> centers;
    Map<Point2D, Point2D> belongings;
    final Object lock = new Object();
    int frame = 0;

    Color[] palette;

    int pointChanges = 0;
    int round = 0;


    public KMeans() {
        super();

        centers = new ArrayList<>();
        belongings = new HashMap<>();

        int groups = 10;

        Random r = new Random();
        for (int i = 0; i < groups; i++) {
            centers.add(new Point2D(width / 2.0 + r.nextGaussian() * 100, height / 2.0 + r.nextGaussian() * 100));
        }

        palette = new Color[]{
                Color.RED,
                Color.YELLOW,
                Color.GREEN,
                Color.CYAN,
                Color.magenta,
                new Color(41, 146, 227),
                new Color(130, 75, 246),
                new Color(246, 136, 26),
                new Color(128, 217, 23),
                Color.blue
        };

        addRandomPoints(100);

        for (int i = 0; i < groups; i++) {
            Vector2D v = Vector2D.fromPolar(Math.min(width, height) / 4.0, (double) i / groups * Math.PI * 2);
            addGaussianPoints(width / 2.0 + v.x, height / 2.0 + v.y, 10, 100);
        }

    }

    void kmeans(Graphics g) throws InterruptedException {
        synchronized (lock) {
            while (true) {
                int i = 0;
                pointChanges = 0;
                for (Point2D point : points) {
                    double distance = 0x7fffffff;
                    Point2D closestPoint = null;
                    for (Point2D center : centers) {
                        Line2D line = new Line2D(center, point);
                        g.setColor(palette[centers.indexOf(center)]);
                        g.drawLine((int) line.start.x, (int) line.start.y, (int) line.end.x, (int) line.end.y);

                        double newDistance = line.getLength();
                        if (newDistance < distance) {
                            distance = newDistance;
                            closestPoint = center;
                            colorMap.put(point, palette[centers.indexOf(center)]);
                        }
                    }
                    if (round < 2) lock.wait();
                    else if (i % (round + 4) == 0) lock.wait();
                    i++;

                    if (!belongings.containsKey(point) || (belongings.get(point) != closestPoint)) {
                        pointChanges++;
                        belongings.put(point, closestPoint);
                    }
                }

                if (pointChanges == 0) break;

                for (Point2D center : centers) {
                    List<Point2D> groupPoints = belongings
                            .entrySet()
                            .stream()
                            .filter(point2DPoint2DEntry -> point2DPoint2DEntry.getValue().equals(center))
                            .map(Map.Entry::getKey)
                            .toList();

                    if (!groupPoints.isEmpty()) {
                        double averageX = groupPoints.stream()
                                .mapToDouble(key -> key.x)
                                .average()
                                .getAsDouble();

                        double averageY = groupPoints.stream()
                                .mapToDouble(key -> key.y)
                                .average()
                                .getAsDouble();


                        List<Line2D> lines = new LinkedList<>();

                        i = 0;
                        for (Point2D point : groupPoints) {
                            lines.add(new Line2D(point.x, point.y, averageX, averageY));
                            g.setColor(palette[centers.indexOf(center)]);
                            for (Line2D line : lines) {
                                g.drawLine((int) line.start.x, (int) line.start.y, (int) line.end.x, (int) line.end.y);
                            }
                            if (round < 2) lock.wait();
                            else if (i % (round + 4) == 0) lock.wait();
                            i++;
                        }

                        center.x = averageX;
                        center.y = averageY;
                    }

                }
                round++;

            }
            lock.wait();
        }
    }

    Thread t;

    @Override
    public void provideFrame(Graphics g) {
        super.provideFrame(g);

        if (frame == 60) {
            Runnable r = () -> {
                try {
                    kmeans(g);
                    finished = true;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            };
            t = new Thread(r);
            t.start();
        }

        for (Point2D center : centers) {
            g.setColor(palette[centers.indexOf(center)]);
            drawPoint(g, (int) center.x, (int) center.y, 5);
        }

        g.setColor(Color.white);
        char[] prompt = String.format("K均值 | 第%d轮 | 本轮改变的点数: %d", round + 1, pointChanges).toCharArray();
        g.drawChars(prompt, 0, prompt.length, 8, 16);
        synchronized (lock) {
            lock.notify();
        }
        frame++;
    }
}
