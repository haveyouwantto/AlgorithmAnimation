package hywt.algo.animation.sorting;

import java.awt.*;

public class InsertionSort extends Sort {
    long compare = 0;

    int ptr = -1;
    int next = -1;

    final Object lock = new Object();

    public InsertionSort() {
        this(2);
    }

    public InsertionSort(int mul) {
        super(mul);
    }

    private void sort(int start, int end) throws InterruptedException {
        synchronized (lock) {
            int next;
            for (next = 1; next < array.size(); next++) {
                this.next = next;
                int current = next, back = next - 1;
                int currentElement, prevElement;
                while (true) {
                    ptr = current;
                    lock.wait();
                    if (back < 0) break;
                    currentElement = array.statsGet(current);
                    prevElement = array.statsGet(back);
                    compare++;
                    if (prevElement > currentElement) {
                        array.statsSet(current, prevElement);
                        array.statsSet(back, currentElement);
                        current--;
                        back--;
                    } else break;
                }
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
        String info = String.format("Insertion Sort (插入排序) - get = %d | set = %d | compare = %d", array.getGets(), array.getSets(), compare);
        g.drawChars(info.toCharArray(), 0, info.length(), 2, 10);
    }

    @Override
    protected void drawList(Graphics g) {

        for (int i = 0; i < array.size(); i++) {
            int val = array.get(i) * heightScale;
            if (i == ptr || i == ptr - 1) {
                g.setColor(Color.RED);
            } else if (i == next + 1) {
                g.setColor(Color.GREEN);
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
