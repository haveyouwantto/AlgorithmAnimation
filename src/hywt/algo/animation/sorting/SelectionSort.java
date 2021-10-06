package hywt.algo.animation.sorting;

import java.awt.*;

public class SelectionSort extends Sort {
    int pointer;
    int selected;
    int max;
    int stop = elements - 1;

    long compare = 0;

    public SelectionSort() {
        this(1);
    }

    public SelectionSort(int mul) {
        super(mul);
    }

    public void provideFrame(Graphics g) {
        super.provideFrame(g);
        drawText(g);
        if (tick > 60) {
            step();
        }
        tick++;
    }

    void step() {
        int value = array.statsGet(pointer);
        compare++;
        if (value > max) {
            selected = pointer;
            max = value;
        }
        if (pointer == stop) {
            array.statsSet(pointer, max);
            array.statsSet(selected, value);
            max = -1;
            selected = -1;
            pointer = -1;
            --stop;
            if (stop <= 0)
                sorted = true;
        }
        pointer++;
    }

    @Override
    public void drawText(Graphics g) {
        g.setColor(Color.WHITE);
        String info = String.format("Selection Sort - get = %d | set = %d | compare = %d", array.getGets(), array.getSets(), compare);
        g.drawChars(info.toCharArray(), 0, info.length(), 2, 10);
    }

    @Override
    protected void drawList(Graphics g) {

        for (int i = 0; i < array.size(); i++) {
            int val = array.get(i) * heightScale;
            if (i == pointer) {
                g.setColor(Color.RED);
            } else if (i == selected) {
                g.setColor(Color.GREEN);
            } else if (i == stop) {
                g.setColor(Color.YELLOW);
            } else {
                g.setColor(Color.WHITE);
            }
            g.fillRect(i * widthScale, height - val, mul < 0? 2 : heightScale, val);
        }
    }

    @Override
    void finish(Graphics g) {
        super.finish(g);
drawText(g);
    }
}
