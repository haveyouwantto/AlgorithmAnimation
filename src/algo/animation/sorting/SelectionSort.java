package algo.animation.sorting;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Collections;

public class SelectionSort extends Sort {
    int pointer;
    int selected;
    int max;
    int stop = elements - 1;

    int compare = 0;
    int tick = 0;

    public SelectionSort(int mul) {
        super(mul);
    }

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
            } else if (i == selected) {
                graphics.setColor(Color.GREEN);
            } else if (i == stop) {
                graphics.setColor(Color.YELLOW);
            } else {
                graphics.setColor(Color.WHITE);
            }
            graphics.fillRect(i * xCons, bim.getHeight() - val, valCons, val);
        }
        graphics.setColor(Color.WHITE);
        String info = String.format("Selection Sort - get = %d | set = %d | compare = %d", array.getGets(), array.getSets(), compare);
        graphics.drawChars(info.toCharArray(), 0, info.length(), 2, 10);
        if (tick > 60) {
            step();
        }
        tick++;
        return bim;
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
    void finish() {
        super.finish();

        String info = String.format("Selection Sort - get = %d | set = %d | compare = %d", array.getGets(), array.getSets(), compare);
        graphics.drawChars(info.toCharArray(), 0, info.length(), 2, 10);
    }
}
