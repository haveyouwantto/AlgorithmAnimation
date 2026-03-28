package hywt.algo.animation.scatter;

import hywt.math.shapes.Point2D;
import java.awt.Color;
import java.awt.Graphics;
import java.util.Comparator;
import java.util.List;
import java.util.ArrayList;

public class PointDistanceSearch extends BasicScatter {
    private final Object lock;
    private Thread thread;

    private int minI, maxI, midI;
    private List<Point2D> pointsXsorted;
    private Point2D currSearch1, currSearch2;
    private Point2D currMin1, currMin2;
    private List<Point2D> stripList;

    private double currMinDistance;
    private double epsilon;
    private double stripMinX, stripMaxX;
    private int t;
    private boolean merging;

    // 添加 volatile 关键字，确保 UI 线程能即时看到算法线程的修改
    private volatile boolean finished;

    private Color leftColor = new Color(0x400000);
    private Color rightColor = new Color(0x002040);
    private Color midColor = new Color(0x404000);

    public PointDistanceSearch() {
        super();
        width = 840;
        height = 360;

        addGappedRandom(1000, 8);
        lock = new Object();
        t = 0;
        currMinDistance = Double.MAX_VALUE;
        merging = false;
        finished = false;

        // 使用更简洁的 Lambda 表达式
        pointsXsorted = points.stream()
                .sorted(Comparator.comparingDouble(p -> p.x))
                .toList();
    }

    private void start() throws InterruptedException {
        synchronized (lock) {
            recursiveSolve(0, points.size() - 1);
            finished = true;
            currSearch1 = null;
            currSearch2 = null;
            lock.wait(); // 等待最后一帧绘制完毕
        }
    }

    private double recursiveSolve(int minI, int maxI) throws InterruptedException {
        this.minI = minI;
        this.maxI = maxI;
        this.midI = maxI; // 拆分前，暂时将 midI 设置为 maxI 用于动画平滑展示
        merging = false;

        synchronized (lock) {
            lock.wait();
        }

        // 如果区间内包含的点数小于等于3个，直接暴力求解
        // maxI - minI <= 2 代表区间长度为 1~3
        if (maxI - minI <= 2) {
            return bruteForce(minI, maxI);
        }

        // 将点集分为两半
        int midI = minI + (maxI - minI) / 2;
        this.minI = minI;
        this.maxI = maxI;
        this.midI = midI;

        synchronized (lock) {
            lock.wait();
        }

        // 修复：右半部分从 midI + 1 开始，避免 midI 被重复计算
        double leftMin = recursiveSolve(minI, midI);
        double rightMin = recursiveSolve(midI + 1, maxI);
        double minDistance = Math.min(leftMin, rightMin);

        // 恢复当前递归深度的动画状态变量
        this.minI = minI;
        this.maxI = maxI;
        this.midI = midI;

        // 合并结果
        epsilon = minDistance;
        merging = true;

        double minX = pointsXsorted.get(minI).x;
        double maxX = pointsXsorted.get(maxI).x;
        double midLine = pointsXsorted.get(midI).x;

        stripMinX = Math.max(minX, midLine - epsilon);
        stripMaxX = Math.min(maxX, midLine + epsilon);

        stripList = null;

        synchronized (lock) {
            lock.wait();
        }

        List<Point2D> strip = new ArrayList<>();

        // 修复：使用 <= 确保最后一个点被包含
        for (int i = minI; i <= maxI; i++) {
            Point2D p = pointsXsorted.get(i);
            if (Math.abs(p.x - midLine) < epsilon) {
                strip.add(p);
            }
        }

        strip.sort(Comparator.comparingDouble(p -> p.y));
        stripList = strip;

        synchronized (lock) {
            lock.wait();
        }

        for (int i = 0; i < strip.size(); i++) {
            for (int j = i + 1; j < strip.size() && (strip.get(j).y - strip.get(i).y) < epsilon; j++) {
                currSearch1 = strip.get(i);
                currSearch2 = strip.get(j);

                synchronized (lock) {
                    lock.wait();
                }

                double distance = calculateDistance(strip.get(i), strip.get(j));
                if (distance < minDistance) {
                    minDistance = distance;
                }
                if (distance < currMinDistance) {
                    currMinDistance = distance;
                    currMin1 = strip.get(i);
                    currMin2 = strip.get(j);
                }
            }
        }

        return minDistance;
    }

    private double bruteForce(int l, int r) throws InterruptedException {
        double minDistance = Double.MAX_VALUE;

        // 修复：使用 <= 确保边界点被计算
        for (int i = l; i <= r; i++) {
            for (int j = i + 1; j <= r; j++) {
                Point2D p1 = pointsXsorted.get(i);
                Point2D p2 = pointsXsorted.get(j);

                currSearch1 = p1;
                currSearch2 = p2;

                synchronized (lock) {
                    lock.wait();
                }

                double distance = calculateDistance(p1, p2);
                if (distance < minDistance) {
                    minDistance = distance;
                }
                if (distance < currMinDistance) {
                    currMinDistance = distance;
                    currMin1 = p1;
                    currMin2 = p2;
                }
            }
        }
        return minDistance;
    }

    // 修复拼写错误并优化距离计算逻辑
    private double calculateDistance(Point2D p1, Point2D p2) {
        return Math.hypot(p1.x - p2.x, p1.y - p2.y);
    }

    @Override
    public void provideFrame(Graphics g) {
        t++;

        if (t == 60) {
            thread = new Thread(() -> {
                try {
                    start();
                } catch (InterruptedException e) {
                    // 优雅处理线程中断
                    Thread.currentThread().interrupt();
                }
            });
            thread.start();
        }

        synchronized (lock) {
            // 手动清屏
            g.setColor(Color.black);
            g.fillRect(0, 0, width, height);

            // 获取所有坐标
            double minX1 = pointsXsorted.get(minI).x;
            double maxX1 = pointsXsorted.get(midI).x;
            double maxX2 = pointsXsorted.get(maxI).x;

            // 绘制左/右背景
            g.setColor(leftColor);
            g.fillRect((int) minX1, 0, (int) (maxX1 - minX1), height);
            g.setColor(rightColor);
            g.fillRect((int) maxX1, 0, (int) (maxX2 - maxX1), height);

            // 绘制中线和合并范围
            if (merging) {
                g.setColor(midColor);
                g.fillRect((int) (stripMinX), 0, (int) (stripMaxX - stripMinX), height);
                g.setColor(Color.yellow);
                g.drawLine((int) maxX1, 0, (int) maxX1, height);
            }

            // 绘制所有点
            g.setColor(Color.white);
            for (Point2D p : points) {
                g.fillOval((int) p.x - 2, (int) p.y - 2, 4, 4);
            }

            // 绘制合并状态下的条带点
            if (merging && stripList != null) {
                g.setColor(Color.yellow);
                for (Point2D p : stripList) {
                    g.fillOval((int) p.x - 2, (int) p.y - 2, 4, 4);
                }
            }

            // 绘制当前正在搜索的两个点（红线）
            g.setColor(Color.red);
            if (currSearch1 != null && currSearch2 != null) {
                g.fillOval((int) currSearch1.x - 2, (int) currSearch1.y - 2, 4, 4);
                g.fillOval((int) currSearch2.x - 2, (int) currSearch2.y - 2, 4, 4);
                g.drawLine((int) currSearch1.x, (int) currSearch1.y, (int) currSearch2.x, (int) currSearch2.y);
            }

            // 绘制当前找到的最短距离点对（绿线）
            g.setColor(Color.green);
            if (currMin1 != null && currMin2 != null) {
                g.fillOval((int) currMin1.x - 2, (int) currMin1.y - 2, 4, 4);
                g.fillOval((int) currMin2.x - 2, (int) currMin2.y - 2, 4, 4);
                g.drawLine((int) currMin1.x, (int) currMin1.y, (int) currMin2.x, (int) currMin2.y);
            }

            // 绘制状态信息
            char[] prompt = String
                    .format("Point Search | currMinDistance: %.2f, minX1: %.2f, mid: %.2f, maxX2: %.2f",
                            currMinDistance > 10000 ? Double.POSITIVE_INFINITY : currMinDistance, minX1, maxX1, maxX2)
                    .toCharArray();
            g.setColor(Color.white);
            g.drawChars(prompt, 0, prompt.length, 4, 16);

            lock.notify();
        }
    }

    @Override
    public boolean hasNext() {
        return !finished;
    }
}