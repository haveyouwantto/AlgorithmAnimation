package hywt.algo.animation.sorting;

import java.awt.*;

public class ShellSort extends Sort {
    int gap;
    int ptr;
    int ptr2;
    int pos;
    int compare = 0;

    final Object lock = new Object();

    public ShellSort() {
        this(1);
    }

    public ShellSort(int mul) {
        super(mul);
        gap = elements / 2;
    }

    void sort() throws InterruptedException {
        synchronized (lock) {
            while (gap >= 1) {
                for (int i = gap; i < elements; i++) {
                    pos = i + 1;
                    int current = i, back = i - gap;
                    while (back >= 0) {
                        ptr = current;
                        ptr2 = back;

                        int currentElement = array.statsGet(current);
                        int prevElement = array.statsGet(back);
                        lock.wait();
                        compare++;
                        if (currentElement < prevElement) {
                            array.statsSet(current, prevElement);
                            array.statsSet(back, currentElement);
                            lock.wait();
                        } else break;


                        current -= gap;
                        back -= gap;
                    }
                }
                gap /= 2;
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
        String info = String.format("Shell Sort - get = %d | set = %d | compare = %d | gap = %d", array.getGets(), array.getSets(), compare, gap);
        g.drawChars(info.toCharArray(), 0, info.length(), 2, 10);
    }

    @Override
    protected void drawList(Graphics g) {
        for (int i = 0; i < array.size(); i++) {
            int val = array.get(i) * heightScale;
            if (i == ptr || i == ptr2) {
                g.setColor(Color.RED);
            } else if (i == pos) {
                g.setColor(Color.GREEN);
            } else if ((i - ptr) % gap == 0) {
                g.setColor(Color.WHITE);
            } else {
                g.setColor(Color.GRAY);
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