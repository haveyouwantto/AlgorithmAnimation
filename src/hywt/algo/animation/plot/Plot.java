package hywt.algo.animation.plot;

import hywt.algo.animation.BasicAnimation;
import hywt.algo.datatype.VideoSize;
import hywt.math.function.Mapper;
import hywt.math.shapes.Line2D;
import hywt.math.shapes.Point2D;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

public abstract class Plot extends BasicAnimation {
    private Point2D center;
    private double magn;
    private Set<Line2D> lines;
    private Mapper mapperX;
    private Mapper mapperY;

    public Plot() {
        super();
        magn = 1;
        lines = new HashSet<>();
        center = new Point2D(0, 0);
        mapperX = new Mapper(-2, 2, 0, 500);
        mapperY = new Mapper(2, -2, 0, 500);
    }

    @Override
    public VideoSize getSize() {
        return new VideoSize(500, 500);
    }

    @Override
    public void provideFrame(Graphics g) {
        super.provideFrame(g);
        for (Line2D line : lines) {
            drawLine2(g, line);
        }
    }

    private int[] getImgPos(double x, double y) {
        return new int[]{(int) mapperX.get(x), (int) mapperY.get(y)};
    }

    public double getPosX(double x) {
        return mapperX.get(x);
    }

    public double getPosY(double y) {
        return mapperY.get(y);
    }

    public double getScaleX(double x) {
        return x * magn * 250;
    }

    public double getScaleY(double y) {
        return y * magn * 250;
    }

    private void drawLine2(Graphics g, Line2D l) {
        int[] p1 = getImgPos(l.start.x, l.start.y);
        int[] p2 = getImgPos(l.end.x, l.end.y);

        g.drawLine(p1[0], p1[1], p2[0], p2[1]);
    }

    public void addLine(double x1, double y1, double x2, double y2) {
        lines.add(new Line2D(x1, y1, x2, y2));
    }

    public void clear() {
        lines.clear();
    }

}
