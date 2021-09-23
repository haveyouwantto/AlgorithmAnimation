package algo.animation.sorting;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;

public class RadixSort extends Sort {
    int tick = 0;
    int compare = 0;

    int arrayWrites = 0;
    int pointer = -1;
    Set<Integer> selected = new HashSet<>();

    final Object lock = new Object();

    public RadixSort(int mul) {
        super(mul);
    }

    private void sort() throws InterruptedException {
        synchronized (lock) {
            int n = 0;
            List<LinkedList<Integer>> buckets = new ArrayList<>();
            for (int i = 0; i < 4; i++) {
                buckets.add(new LinkedList<>());
            }
            while (n < Math.log(elements) / Math.log(4)) {
                for (pointer = 0; pointer < elements; pointer++) {
                    int e = array.statsGet(pointer);
                    lock.wait();
                    buckets.get(((e >> (n * 2)) & 0b11)).add(e);
                    arrayWrites++;
                }
                pointer = 0;
                for (LinkedList<Integer> bucket : buckets) {
                    for (Integer integer : bucket) {
                        array.statsSet(pointer, integer);
                        lock.wait();
                        pointer++;
                    }
                    bucket.clear();
                }
                n++;
            }
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

            if (i == pointer) {
                graphics.setColor(Color.RED);
            } else if (selected.contains(i)) {
                graphics.setColor(Color.GREEN);
            } else {
                graphics.setColor(Color.WHITE);
            }

            graphics.fillRect(i * xCons, bim.getHeight() - val, valCons, val);
        }
        graphics.setColor(Color.WHITE);
        String info = String.format("Radix Sort - get = %d | set = %d | compare = %d | aux set = %d", array.getGets(), array.getSets(), compare, arrayWrites);
        graphics.drawChars(info.toCharArray(), 0, info.length(), 2, 10);
        if (tick == 60) {
            Runnable r = () -> {
                try {
                    sort();
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

        String info = String.format("Radix Sort - get = %d | set = %d | compare = %d | aux set = %d", array.getGets(), array.getSets(), compare, arrayWrites);
        graphics.drawChars(info.toCharArray(), 0, info.length(), 2, 10);
    }
}
