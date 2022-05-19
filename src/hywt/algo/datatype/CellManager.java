package hywt.algo.datatype;

import java.util.Arrays;

public class CellManager {
    private final int[] cells;
    private final int width;
    private final int height;
    private Updater updater;


    @FunctionalInterface
    public interface Updater {
        int update(int v);
    }

    public CellManager(int width, int height) {
        this.width = width;
        this.height = height;
        this.cells = new int[width * height];
        Arrays.fill(cells, 100);
    }

    public int get(int x, int y) {
        return cells[y * height + x];
    }

    public void setUpdater(Updater updater) {
        this.updater = updater;
    }

    public void update() {
        for (int i = 0; i < cells.length; i++) {
            int newValue = updater.update(cells[i]);
            if (newValue > 200) newValue = 200;
            else if (newValue < 0) newValue = 0;
            cells[i] = newValue;
        }
    }

    public int[] getCells() {
        return cells;
    }

    public void getFrequency(double[] arr) {
        for (int s : cells) {
            arr[s]++;
        }
        for (int i = 0; i < arr.length; i++) {
            arr[i] = arr[i] / (width * height);
        }
    }
}
