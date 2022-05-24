package hywt.algo.datatype;

import java.util.Arrays;
import java.util.Iterator;

public class CellManager implements Iterable<Integer> {
    private final int[] cells;
    private final int width;
    private final int height;
    private Updater updater;

    @FunctionalInterface
    public interface Updater {
        void update();
    }

    public CellManager(int width, int height) {
        this.width = width;
        this.height = height;
        this.cells = new int[width * height];
    }

    public void fill(int val) {
        Arrays.fill(cells, val);
    }

    public int get(int x, int y) {
        return cells[y * height + x];
    }

    public void setUpdater(Updater updater) {
        this.updater = updater;
    }

    public void update() {
        updater.update();
    }

    public int get(int index) {
        return cells[index];
    }

    public void set(int index, int value) {
        if (value > 200) {
            cells[index] = 200;
            return;
        } else if (value < 0) {
            cells[index] = 0;
            return;
        }
        cells[index] = value;
    }

    public void getFrequency(double[] arr) {
        for (int s : cells) {
            arr[s]++;
        }
        for (int i = 0; i < arr.length; i++) {
            arr[i] = arr[i] / (width * height);
        }
    }

    public int size() {
        return cells.length;
    }

    @Override
    public Iterator<Integer> iterator() {
        return Arrays.stream(cells).iterator();
    }
}
