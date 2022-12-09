package hywt.algo.animation.sorting;

import java.awt.*;
import java.util.List;
import java.util.*;

public class RadixSort extends Sort {
    long compare = 0;

    int arrayWrites = 0;
    int pointer = -1;
    Set<Integer> selected = new HashSet<>();
    List<LinkedList<Integer>> buckets;

    private final Color[] palette = {
            new Color(0xEF4426),
            new Color(0xECEC2C),
            new Color(0x27F127),
            new Color(0x1C95FF)
    };

    final Object lock = new Object();

    public RadixSort() {
        this(1);
    }

    public RadixSort(int mul) {
        super(mul);
    }

    private void sort() throws InterruptedException {
        synchronized (lock) {
            int n = 0;
            buckets = new ArrayList<>();
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
                }
                for (int i = 0; i < 4; i++) {
                    buckets.get(i).clear();
                }
                n++;
            }
            lock.wait();
        }
    }

    Thread sort;

    public void provideFrame(Graphics g) {
        super.provideFrame(g);
        if (tick == 60) {
            Runnable r = () -> {
                try {
                    sort();
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
        String info = String.format("Radix Sort - get = %d | set = %d | compare = %d | aux set = %d", array.getGets(), array.getSets(), compare, arrayWrites);
        g.drawChars(info.toCharArray(), 0, info.length(), 2, 10);
    }

    @Override
    protected void drawList(Graphics g) {

        for (int i = 0; i < array.size(); i++) {
            int val = array.get(i) * heightScale;

            g.setColor(Color.WHITE);


            if (buckets != null) {
                for (int j = 0; j < 4; j++) {
                    if (buckets.get(j).contains(array.get(i))) {
                        g.setColor(palette[j]);
                        break;
                    }
                }
            }

            if (i == pointer) {
                g.setColor(Color.MAGENTA);
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
