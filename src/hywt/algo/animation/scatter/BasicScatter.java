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

    public void addGappedRandom(double pts, double minDist) {
        Random r = new Random();
        int maxAttempts = 100; // 每个点的最大尝试次数
        double minDistSquared = minDist * minDist; // 使用平方距离避免开方运算

        for (int i = 0; i < pts; i++) {
            int attempts = 0;
            boolean placed = false;

            while (attempts < maxAttempts && !placed) {
                // 生成候选点
                double x = r.nextDouble() * width;
                double y = r.nextDouble() * height;
                Point2D candidate = new Point2D(x, y);

                // 检查是否与已有点距离足够
                boolean valid = true;
                for (Point2D existing : points) {
                    double dx = x - existing.x;
                    double dy = y - existing.y;
                    if (dx * dx + dy * dy < minDistSquared) {
                        valid = false;
                        break;
                    }
                }

                // 如果距离足够，添加点
                if (valid) {
                    points.add(candidate);
                    placed = true;
                }

                attempts++;
            }

            // 如果尝试多次仍无法放置，可以跳过或调整策略
            if (!placed) {
                // 可选：放宽条件或记录警告
                System.out.println("Warning: Failed to place point " + i + " after " + maxAttempts + " attempts");
            }
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
