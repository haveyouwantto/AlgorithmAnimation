package hywt.algo.animation.sorting;

import java.awt.*;

public class StoogeSort extends Sort {
    long compare = 0;

    int end = -1;
    int start = -1;

    final Object lock = new Object();

    public StoogeSort() {
        this(1);
    }

    public StoogeSort(int mul) {
        super(mul);
    }

    private void sort(int start, int end) throws InterruptedException {
        synchronized (lock) {
            int l = array.statsGet(start);
            this.start = start;
            int r = array.statsGet(end);
            this.end = end;

            // Uncomment below line to disable skipping
            // lock.wait();

            compare++;
            if (r < l) {
                array.statsSet(start, r);
                array.statsSet(end, l);
                lock.wait();
            }
            if (end - start + 1 >= 3) {
                int t = (end - start + 1) / 3;
                sort(start, end - t);
                sort(start + t, end);
                sort(start, end - t);
            }
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
        String info = String.format("Stooge Sort - get = %d | set = %d | compare = %d", array.getGets(), array.getSets(), compare);
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

            g.fillRect(i * widthScale, height - val, mul < 0 ? 2 : heightScale, val);
        }
    }

    @Override
    void finish(Graphics g) {
        super.finish(g);
        drawText(g);
    }
}
