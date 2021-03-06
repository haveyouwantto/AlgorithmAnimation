package hywt.algo.animation.sorting;

import java.awt.*;

public class SlowSort extends Sort {
    long compare = 0;

    int end = -1;
    int start = -1;

    final Object lock = new Object();

    public SlowSort() {
        this(1);
    }

    public SlowSort(int mul) {
        super(mul);
    }

    private void sort(int start, int end) throws InterruptedException {
        synchronized (lock) {
            if (start >= end) {
                return;
            }
            int mid = (start + end) / 2;
            sort(start, mid);
            sort(mid + 1, end);

            int endVal = array.statsGet(end);
            int midVal = array.statsGet(mid);
            this.start = start;
            this.end = end;

            // Uncomment below line to disable skipping
            // lock.wait();

            compare++;
            if (midVal > endVal) {
                array.statsSet(mid, endVal);
                array.statsSet(end, midVal);
                lock.wait();
            }
            sort(start, end - 1);
        }
    }

    Thread sort;

    public void provideFrame(Graphics g) {
        super.provideFrame(g);
        drawText(g);
        if (tick == 60) {
            Runnable r = () -> {
                try {
                    sort(0, array.size() - 1);
                    sorted = true;
                    finish(g);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            };
            sort = new Thread(r);
            sort.start();
        } else if (tick > 60) {
            synchronized (lock) {
                lock.notify();
            }
        }
        tick++;
    }

    @Override
    public void drawText(Graphics g) {

        g.setColor(Color.WHITE);
        String info = String.format("Slow Sort - get = %d | set = %d | compare = %d", array.getGets(), array.getSets(), compare);
        g.drawChars(info.toCharArray(), 0, info.length(), 2, 10);
    }

    @Override
    protected void drawList(Graphics g) {

        for (int i = 0; i < array.size(); i++) {
            int val = array.get(i) * heightScale;

            if (i == this.end) {
                g.setColor(Color.YELLOW);
            } else if (i == this.start) {
                g.setColor(Color.CYAN);
            } else {
                g.setColor(Color.WHITE);
            }
            g.fillRect(i * widthScale, height - val, mul < 0? 2 : heightScale, val);
        }
    }

    @Override
    void finish(Graphics g) {
        super.finish(g);
        drawText(g);
    }
}
