package algo.animation.sorting;

import algo.animation.AnimationGen;
import algo.datatype.StatsArrayList;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Collections;

public abstract class Sort implements AnimationGen {
    StatsArrayList<Integer> array = new StatsArrayList<>();
    int elements = 80;
    BufferedImage bim = new BufferedImage(640, 480, BufferedImage.TYPE_INT_RGB);
    Graphics graphics = bim.getGraphics();
    boolean sorted;
    boolean finished;

    public Sort() {
        for (int i = 0; i < elements; i++) {
            array.add(i + 1);
        }
        Collections.shuffle(array);
    }

    @Override
    public boolean hasNext() {
        return !(sorted && finished);
    }

    int finish;

    void finish() {
        graphics.setColor(Color.GREEN);
        for (int i = 0; i < array.size(); i++) {
            int val = array.get(i) * 6;
            if (i == finish) {
                graphics.setColor(Color.WHITE);
            }
            graphics.fillRect(i * 8, bim.getHeight() - val, 6, val);
        }
        finish++;
        if (finish == array.size()) finished = true;
    }

    abstract void step();

}
