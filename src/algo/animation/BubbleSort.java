package algo.animation;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BubbleSort implements AnimationGen {
    List<Integer> array = new ArrayList<>();
    int elements = 80;
    BufferedImage bim = new BufferedImage(640, 480, BufferedImage.TYPE_INT_RGB);
    Graphics graphics = bim.getGraphics();
    boolean sorted;

    int pointer = 0;
    int stop = elements - 1;

    int get = 0;
    int set = 0;
    int compare = 0;

    int tick = 0;

    public BubbleSort() {
        for (int i = 0; i < elements; i++) {
            array.add(i + 1);
        }
        Collections.shuffle(array);
    }

    @Override
    public boolean hasNext() {
        return !sorted;
    }

    private void step() {
        int next = array.get(pointer + 1);
        int current = array.get(pointer);
        get += 2;
        compare++;
        if (next < current) {
            array.set(pointer, next);
            array.set(pointer + 1, current);
            set += 2;
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
        for (int i = 0; i < array.size(); i++) {
            int val = array.get(i) * 6;
            if (i == pointer) {
                graphics.setColor(Color.RED);
            } else if (i == pointer + 1) {
                graphics.setColor(Color.GREEN);
            } else if (i == stop) {
                graphics.setColor(Color.YELLOW);
            } else {
                graphics.setColor(Color.WHITE);
            }
            graphics.fillRect(i * 8, bim.getHeight() - val, 6, val);
        }
        graphics.setColor(Color.WHITE);
        String info = String.format("Bubble Sort - get = %d | set = %d | compare = %d", get, set, compare);
        graphics.drawChars(info.toCharArray(), 0, info.length(), 2, 10);
        if (tick > 60) {
            step();
        }
        tick++;
        return bim;
    }
}
