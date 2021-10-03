package hywt.algo.animation.sorting;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

public class QuickSort extends Sort {
    int tick = 0;
    long compare = 0;

    int pivot = -1;
    int start = -1;
    Set<Integer> pointers = new HashSet<>();
    Set<Integer> selected = new HashSet<>();
    Set<Integer> sortedInt = new HashSet<>();

    final Object lock = new Object();

    public QuickSort() {
        this(1);
    }

    public QuickSort(int mul) {
        super(mul);
    }

    private void sort(int start, int end) throws InterruptedException {
        synchronized (lock) {
            this.start = start;
            if (end - start <= 0) {
                sortedInt.add(start);
                lock.wait();
                return;
            }

            //select pivot
            int pivot = start + (end - start) / 2;
            int pivolValue = array.statsGet(pivot);
            this.pivot = pivot;
            lock.wait();

            // move pivot to the end
            array.statsSet(pivot, array.statsGet(end));
            array.statsSet(end, pivolValue);
            pivot = end;
            this.pivot = end;
            lock.wait();

            int lptr = -1, rptr = -1;
            int llast = start, rlast = end - 1;
            while (true) {
                // find the element lesser than pivot
                for (int i = llast; i < end; i++) {
                    pointers.add(i);
                    lock.wait();

                    pointers.clear();
                    compare++;
                    if (array.statsGet(i) > pivolValue) {
                        lptr = i;
                        selected.add(i);
                        lock.wait();

                        break;
                    }
                }

                // if not found, exit the loop
                if (lptr == -1) {
                    sortedInt.add(end);
                    lock.wait();
                    selected.clear();
                    break;
                }

                // find the element greater than pivot
                for (int i = rlast; i >= start; i--) {
                    pointers.add(i);
                    lock.wait();

                    pointers.clear();
                    compare++;
                    if (array.statsGet(i) < pivolValue) {
                        rptr = i;
                        selected.add(i);
                        lock.wait();

                        break;
                    }
                }
                // if the lesser element has greater index than the greater element
                if (lptr > rptr) {
                    array.statsSwap(lptr, end);
                    pivot = lptr;
                    this.pivot = lptr;
                    sortedInt.add(lptr);
                    lock.wait();
                    selected.clear();
                    break;
                } else if (rptr == 99999) { // if greater element not found
                    array.statsSwap(start, end);
                    pivot = start;
                    this.pivot = start;
                    sortedInt.add(start);
                    lock.wait();
                    selected.clear();
                    break;
                }
                // swap the lesser and greater element
                array.statsSwap(lptr, rptr);
                llast = lptr + 1;
                rlast = rptr - 1;
                selected.clear();
                lock.wait();
            }
            // recursive sort 2 sub-lists
            sort(start, pivot - 1);
            sort(pivot + 1, end);
            lock.wait();
        }
    }

    Thread sort;

    public void provideFrame(Graphics g) {
        super.provideFrame(g);
        if (sorted) {
            finish(g);
            return;
        }
        for (int i = 0; i < array.size(); i++) {
            int val = array.get(i) * heightScale;

            if (pointers.contains(i)) {
                g.setColor(Color.RED);
            } else if (i == this.pivot) {
                g.setColor(Color.YELLOW);
            } else if (i == this.start) {
                g.setColor(Color.CYAN);
            } else if (selected.contains(i)) {
                g.setColor(Color.GREEN);
            } else {
                g.setColor(Color.WHITE);
            }

            g.fillRect(i * widthScale, height - val, mul < 0? 2 : heightScale, val);
        }
        g.setColor(Color.WHITE);
        String info = String.format("Quick Sort - get = %d | set = %d | compare = %d", array.getGets(), array.getSets(), compare);
        g.drawChars(info.toCharArray(), 0, info.length(), 2, 10);
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
    void finish(Graphics g) {
        super.finish(g);

        String info = String.format("Quick Sort - get = %d | set = %d | compare = %d", array.getGets(), array.getSets(), compare);
        g.drawChars(info.toCharArray(), 0, info.length(), 2, 10);
    }
}
