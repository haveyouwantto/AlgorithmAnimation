package hywt.algo.animation.sorting;

import java.awt.*;

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
    public void provideFrame(Graphics g) {
        super.provideFrame(g);
        if (sorted) {
            finish(g);
            return;
        }
        for (int i = 0; i < array.size(); i++) {
            int val = array.get(i) * valCons;
            if (i == pointer || i == pointer + 1) {
                g.setColor(Color.RED);
            } else if (i == stop) {
                g.setColor(Color.YELLOW);
            } else {
                g.setColor(Color.WHITE);
            }
            g.fillRect(i * xCons, height - val, valCons, val);
        }
        g.setColor(Color.WHITE);
        String info = String.format("Bubble Sort - get = %d | set = %d | compare = %d", array.getGets(), array.getSets(), compare);
        g.drawChars(info.toCharArray(), 0, info.length(), 2, 10);
        if (tick > 60) {
            step();
        }
        tick++;
        return;
    }

    @Override
    void finish(Graphics g) {
        super.finish(g);

        String info = String.format("Bubble Sort - get = %d | set = %d | compare = %d", array.getGets(), array.getSets(), compare);
        g.drawChars(info.toCharArray(), 0, info.length(), 2, 10);
    }
}
