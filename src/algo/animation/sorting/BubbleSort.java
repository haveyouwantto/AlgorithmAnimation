package algo.animation.sorting;

import algo.animation.AnimationGen;
import algo.datatype.StatsArrayList;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Collections;

public class BubbleSort extends Sort {

    int pointer = 0;
    int stop = elements - 1;

    int compare = 0;

    int tick = 0;

    public BubbleSort(int mul) {
        super(mul);
    }


    void step() {
        int next = array.statsGet(pointer + 1);
        int current = array.statsGet(pointer);
        compare++;
        if (next < current) {
            array.statsSet(pointer, next);
            array.statsSet(pointer + 1, current);
        }
        if (++pointer >= stop) {
            pointer = 0;
            --stop;
            if (stop <= 1) sorted = true;
        }
    }

    @Override
    public BufferedImage provideFrame() {
        graphics.setColor(Color.BLACK);
        graphics.fillRect(0, 0, bim.getWidth(), bim.getHeight());
        if (sorted) {
            finish();
            return bim;
        }
        for (int i = 0; i < array.size(); i++) {
            int val = array.get(i) * valCons;
            if (i == pointer || i == pointer + 1) {
                graphics.setColor(Color.RED);
            } else if (i == stop) {
                graphics.setColor(Color.YELLOW);
            } else {
                graphics.setColor(Color.WHITE);
            }
            graphics.fillRect(i * xCons, bim.getHeight() - val, valCons, val);
        }
        graphics.setColor(Color.WHITE);
        String info = String.format("Bubble Sort - get = %d | set = %d | compare = %d", array.getGets(), array.getSets(), compare);
        graphics.drawChars(info.toCharArray(), 0, info.length(), 2, 10);
        if (tick > 60) {
            step();
        }
        tick++;
        return bim;
    }

    @Override
    void finish() {
        super.finish();

        String info = String.format("Bubble Sort - get = %d | set = %d | compare = %d", array.getGets(), array.getSets(), compare);
        graphics.drawChars(info.toCharArray(), 0, info.length(), 2, 10);
    }
}
