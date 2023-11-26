package hywt.algo.animation.scatter;

import hywt.algo.animation.BasicAnimation;
import hywt.algo.datatype.VideoSize;
import hywt.math.shapes.Point2D;

import java.awt.*;
import java.util.*;
import java.util.List;

public class BasicScatter extends BasicAnimation {
    List<Point2D> points;
    Map<Point2D, Color> colorMap;

    int width;
    int height;
    boolean finished = false;

    public BasicScatter() {
        points = new LinkedList<>();
        colorMap = new HashMap<>();

        width = 700;
        height = 700;
    }

    public void addGaussianPoints(double xCenter, double yCenter, double derivation, double pts) {
        Random r = new Random();
        for (int i = 0; i < pts; i++) {
            double x = xCenter + derivation * r.nextGaussian();
            double y = yCenter + derivation * r.nextGaussian();
            points.add(new Point2D(x, y));
        }
    }

    public void addRandomPoints(double pts) {
        Random r = new Random();
        for (int i = 0; i < pts; i++) {
            double x = r.nextDouble()*width;
            double y = r.nextDouble()*height;
            points.add(new Point2D(x, y));
        }
    }

    @Override
    public boolean hasNext() {
        return !finished;
    }

    @Override
    public VideoSize getSize() {
        return new VideoSize(width, height);
    }

    void drawPoint(Graphics g, int x, int y, int radius) {
        g.fillOval(x - radius, y - radius, radius * 2, radius * 2);
    }

    @Override
    public void provideFrame(Graphics g) {
        super.provideFrame(g);
        for (Point2D point : points) {
            g.setColor(colorMap.getOrDefault(point, Color.WHITE));

            drawPoint(g, (int) point.x, (int) point.y, 2);
        }
    }
}
