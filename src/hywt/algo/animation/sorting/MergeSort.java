package hywt.algo.animation.sorting;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

public class MergeSort extends Sort {
    long compare = 0;

    int end = -1;
    int start = -1;
    int mid = -1;
    int arrayWrites = 0;
    Set<Integer> pointers = new HashSet<>();
    Set<Integer> selected = new HashSet<>();

    final Object lock = new Object();

    public MergeSort() {
        this(1);
    }

    public MergeSort(int mul) {
        super(mul);
    }

    private void sort(int start, int end) throws InterruptedException {
        synchronized (lock) {
            int elements = end - start + 1;
            int mid = start + (end - start) / 2;
            this.start = start;
            this.end = end;
            this.mid = mid + 1;
            lock.wait();
            if (end != start) {
                sort(start, mid);
                sort(mid + 1, end);

                int lptr = start;
                int rptr = mid + 1;
                int[] aux = new int[elements];
                int i = 0;

                this.start = start;
                this.end = end;
                this.mid = mid + 1;
                while (true) {
                    if (lptr <= mid && rptr <= end) {
                        int lptrs = array.statsGet(lptr);
                        pointers.add(lptr);
                        int rptrs = array.statsGet(rptr);
                        pointers.add(rptr);
                        compare++;
                        if (lptrs < rptrs) {
                            aux[i] = lptrs;
                            arrayWrites++;
                            lptr++;
                        } else {
                            aux[i] = rptrs;
                            arrayWrites++;
                            rptr++;
                        }
                    } else {
                        if (rptr > end) {
                            for (int j = lptr; j <= mid; j++) {
                                aux[i] = array.statsGet(j);
                                arrayWrites++;
                                i++;
                                pointers.add(j);
                                lock.wait();
                                pointers.clear();
                            }
                        }
                        if (lptr > mid) {
                            for (int j = rptr; j <= end; j++) {
                                aux[i] = array.statsGet(j);
                                arrayWrites++;
                                i++;
                                pointers.add(j);
                                lock.wait();
                                pointers.clear();
                            }
                        }
                        break;
                    }
                    i++;
                    lock.wait();
                    pointers.clear();
                }
                this.mid = -1;
                for (i = 0; i < elements; i++) {
                    array.statsSet(i + start, aux[i]);
                    pointers.add(i + start);
                    lock.wait();
                    pointers.clear();
                }
            }
        }
    }

    Thread sort;

    public void provideFrame(Graphics g) {
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
        String info = String.format("Merge Sort - get = %d | set = %d | compare = %d | aux set = %d", array.getGets(), array.getSets(), compare, arrayWrites);
        g.drawChars(info.toCharArray(), 0, info.length(), 2, 10);
    }

    @Override
    protected void drawList(Graphics g) {
        for (int i = 0; i < array.size(); i++) {
            int val = array.get(i) * heightScale;

            if (pointers.contains(i)) {
                g.setColor(Color.RED);
            } else if (i == this.end) {
                g.setColor(Color.YELLOW);
            } else if (i == this.start || i == this.mid) {
                g.setColor(Color.CYAN);
            } else if (selected.contains(i)) {
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
