package hywt.algo.animation.sorting;

import java.awt.*;

public abstract class ThreadedSort extends Sort {

    final Object lock = new Object();
    Thread sorting;

    public ThreadedSort() {
        super();
    }
    public ThreadedSort(int mul) {
        super(mul);
    }

    @Override
    public final void provideFrame(Graphics g) {
        super.provideFrame(g);
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
            sorting = new Thread(r);
            sorting.start();
        } else if (tick > 60) {
            synchronized (lock) {
                lock.notify();
            }
        }
        tick++;
    }

    protected abstract void sort(int start, int end) throws InterruptedException;
}
