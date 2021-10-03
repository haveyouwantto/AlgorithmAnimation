package hywt.algo.animation.sorting;

import java.awt.*;
import java.awt.image.BufferedImage;

public class SlowSort extends Sort {
    int tick = 0;
    int compare = 0;

    int end = -1;
    int start = -1;

    final Object lock = new Object();

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
            lock.wait();

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

    public BufferedImage provideFrame() {
        graphics.setColor(Color.BLACK);
        graphics.fillRect(0, 0, bim.getWidth(), bim.getHeight());
        if (sorted) {
            finish();
            return bim;
        }
        for (int i = 0; i < array.size(); i++) {
            int val = array.get(i) * valCons;

            if (i == this.end) {
                graphics.setColor(Color.YELLOW);
            } else if (i == this.start) {
                graphics.setColor(Color.CYAN);
            } else {
                graphics.setColor(Color.WHITE);
            }

            graphics.fillRect(i * xCons, bim.getHeight() - val, valCons, val);
        }
        graphics.setColor(Color.WHITE);
        String info = String.format("Slow Sort - get = %d | set = %d | compare = %d", array.getGets(), array.getSets(), compare);
        graphics.drawChars(info.toCharArray(), 0, info.length(), 2, 10);
        if (tick == 60) {
            Runnable r = () -> {
                try {
                    sort(0, array.size() - 1);
                    sorted = true;
                    finish();
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
        return bim;
    }

    @Override
    void finish() {
        super.finish();
        String info = String.format("Slow Sort - get = %d | set = %d | compare = %d", array.getGets(), array.getSets(), compare);
        graphics.drawChars(info.toCharArray(), 0, info.length(), 2, 10);
    }
}
