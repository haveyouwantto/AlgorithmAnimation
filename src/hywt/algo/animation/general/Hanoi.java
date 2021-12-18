package hywt.algo.animation.general;

import hywt.algo.animation.BasicAnimation;
import hywt.algo.datatype.Stack;
import hywt.algo.datatype.VideoSize;

import java.awt.*;

public class Hanoi extends BasicAnimation {
    private Stack<Integer> a;
    private Stack<Integer> b;
    private Stack<Integer> c;

    private Stack<Movement> movements;

    private final int width = 640;
    private final int height = 480;

    private final int xStep;

    long tick = 0;
    boolean finished = false;

    final Object lock = new Object();
    Thread thread;
    int moves = 0;

    public Hanoi() {
        this(10);
    }

    public Hanoi(int height) {
        super();
        a = new Stack<>();
        b = new Stack<>();
        c = new Stack<>();
        movements = new Stack<>();

        for (int i = height; i > 0; i--) {
            a.push(i);
        }
        xStep = width / 3;
    }

    public void hanoi(int n, Stack<Integer> src, Stack<Integer> buf, Stack<Integer> dst) throws InterruptedException {
        synchronized (lock) {
            movements.push(new Movement(n, getOrd(src), getOrd(buf), getOrd(dst)));
            lock.wait();
            if (n == 1) {
                dst.push(src.pop());
                moves++;
                lock.wait();
            } else {
                hanoi(n - 1, src, dst, buf);
                hanoi(1, src, buf, dst);
                hanoi(n - 1, buf, src, dst);
            }
            movements.pop();
            lock.wait();
        }
    }

    @Override
    public void provideFrame(Graphics g) {
        super.provideFrame(g);
        g.setColor(Color.WHITE);
        drawMovements(g);
        drawTower(g, a, getPos(1));
        drawTower(g, b, getPos(2));
        drawTower(g, c, getPos(3));

        String info = String.format("Tower of Hanoi - moves=%d", moves);
        g.drawChars(info.toCharArray(), 0, info.length(), 4, 12);

        if (tick == 60) {
            thread = new Thread(() -> {
                try {
                    hanoi(a.size(), a, b, c);
                    finish();
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

        tick++;
    }

    private int getPos(int ord) {
        int m = xStep / 2;
        return xStep * ord - m;
    }

    private void finish() {
        finished = true;
    }

    public void drawTower(Graphics g, Stack<Integer> stack, int posX) {
        int y = 25;
        g.drawLine(posX, height, posX, height / 2);
        for (int i = 0; i < stack.size(); i++) {
            int e = stack.get(i);
            g.fillRect(posX - e * 5, height - y, e * 10, 20);

            g.setColor(Color.BLACK);
            FontMetrics metrics = g.getFontMetrics();
            char[] ord = String.valueOf(e).toCharArray();
            g.drawChars(ord, 0, ord.length, posX - metrics.charsWidth(ord, 0, ord.length) / 2, height - y + 12);

            g.setColor(Color.WHITE);
            y += 25;
        }
    }

    public void drawMovements(Graphics g) {
        for (int i = 0; i < 16; i++) {
            if (movements.size() <= i || movements.peek(i).n > 14) break;
            Movement m = movements.peek(i);
            int y = height / 2 - m.n * 15;
            int srcPos = getPos(m.src);
            int dstPos = getPos(m.dst);
            int bufPos = getPos(m.buf);

            // Draw src -> dst line
            g.drawLine(srcPos, y, dstPos, y);
            g.drawLine(srcPos, y, srcPos, y + 5);

            // Draw dst arrow
            g.drawLine(dstPos, y, dstPos, y + 5);
            g.fillPolygon(new int[]{dstPos - 3, dstPos + 4, dstPos}, new int[]{y + 3, y + 3, y + 9}, 3);

            if (m.n > 1) {
                char[] ord = String.valueOf(m.n).toCharArray();
                g.drawChars(ord, 0, ord.length, 80, y + 4);
                g.drawLine(75, y, 75 - m.n * 5, y);

                g.setColor(Color.GREEN);

                // Draw src -> buf line
                g.drawLine(srcPos, y, srcPos, y - 2);
                g.drawLine(bufPos, y, bufPos, y - 2);
                g.drawLine(srcPos, y - 2, bufPos, y - 2);

                // Draw buf arrow
                g.drawLine(bufPos, y, bufPos, y + 5);
                g.fillPolygon(new int[]{bufPos - 3, bufPos + 4, bufPos}, new int[]{y + 3, y + 3, y + 9}, 3);

            }

            if (getStack(m.dst).contains(m.n)) {
                g.setColor(Color.RED);
                g.drawLine(540, y, 545, y + 8);
                g.drawLine(545, y + 8, 555, y - 2);
            }

            g.setColor(Color.WHITE);
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

    private int getOrd(Stack<Integer> stack) {
        if (a.equals(stack)) {
            return 1;
        } else if (b.equals(stack)) {
            return 2;
        } else if (c.equals(stack)) {
            return 3;
        }
        return -1;
    }

    private Stack<Integer> getStack(int ord) {
        switch (ord) {
            case 1:
                return a;
            case 2:
                return b;
            case 3:
                return c;
            default:
                return null;
        }
    }

    static class Movement {
        final int n;
        final int src;
        final int buf;
        final int dst;

        Movement(int n, int src, int buf, int dst) {
            this.n = n;
            this.src = src;
            this.buf = buf;
            this.dst = dst;
        }
    }
}
