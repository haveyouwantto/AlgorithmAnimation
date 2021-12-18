package hywt.algo.animation.graph;

import hywt.algo.datatype.Graph;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

public class RecursiveSplitGenerator extends EmptyGraph {
    final Object lock = new Object();
    Thread thread;
    boolean finished = false;
    int x1;
    int y1;
    int x2;
    int y2;


    public RecursiveSplitGenerator() throws IOException {
        super();
        start();
    }

    @Override
    public boolean hasNext() {
        return !finished;
    }

    @Override
    public void provideFrame(Graphics g) {
        super.provideFrame(g);
        String info = String.format("递归分割迷宫生成器 - (%d,%d) | (%d,%d)", x1, y1, x2, y2);
        g.drawChars(info.toCharArray(), 0, info.length(), 4, 12);
        if (tick == 150) {
            thread = new Thread(() -> {
                try {
                    split(0, 0, graph.getWidth() - 1, graph.getHeight() - 1);
                    finished = true;
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

    Random r = new Random();

    private int randRange(int s, int e) {
        if (e == s) return e;
        return r.nextInt(e - s) + s;
    }

    public void split(int x1, int y1, int x2, int y2) throws InterruptedException {
        synchronized (lock) {
            this.x1 = x1;
            this.x2 = x2;
            this.y1 = y1;
            this.y2 = y2;
            if (x2 - x1 <= 3 || y2 - y1 <= 3) return;

            int xs = (randRange(x1 + 2, x2 - 2) | 1) - 1;
            int ys = (randRange(y1 + 2, y2 - 2) | 1) - 1;

            graph.drawHorizontal(x1 + 1, x2 - 1, ys, Graph.WALL);
            lock.wait();
            graph.drawVertical(xs, y1 + 1, y2 - 1, Graph.WALL);
            lock.wait();

            int xh1 = randRange(x1 + 1, xs - 1) | 1;
            int xh2 = randRange(xs + 1, x2 - 1) | 1;
            int yh1 = randRange(y1 + 1, ys - 1) | 1;
            int yh2 = randRange(ys + 1, y2 - 1) | 1;

            LinkedList<Integer> rand = new LinkedList<>();
            for (int i = 0; i < 4; i++) {
                rand.add(i);
            }

            for (int i = 0; i < 3; i++) {
                int index = r.nextInt(rand.size());
                switch (rand.remove(index)) {
                    case 0:
                        graph.set(xh1, ys, Graph.EMPTY);
                        lock.wait();
                        break;
                    case 1:
                        graph.set(xh2, ys, Graph.EMPTY);
                        lock.wait();
                        break;
                    case 2:
                        graph.set(xs, yh1, Graph.EMPTY);
                        lock.wait();
                        break;
                    case 3:
                        graph.set(xs, yh2, Graph.EMPTY);
                        lock.wait();
                        break;
                }
            }

            split(x1, y1, xs, ys);
            split(xs, y1, x2, ys);
            split(x1, ys, xs, y2);
            split(xs, ys, x2, y2);
        }
    }

    public void start() {
        graph.setStart(1, 1);
        graph.setEnd(graph.getWidth() - 2, graph.getHeight() - 2);
    }

    public Graph getGraph() {
        return graph;
    }

}
