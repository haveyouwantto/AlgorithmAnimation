package hywt.algo.animation.sorting;

import algo.animation.AnimationGen;
import algo.datatype.VideoSize;
import algo.datatype.StatsArrayList;

import java.awt.*;
import java.util.Collections;

public abstract class Sort implements AnimationGen {
    StatsArrayList<Integer> array = new StatsArrayList<>();
    int elements;
    int mul;
    int valCons;
    int xCons;
    boolean sorted;
    boolean finished;

    int width = 640;
    int height = 480;

    public Sort(int mul) {
        this.mul = mul;
        if (160 % mul != 0) throw new ArithmeticException("mul must be dividable by 160");
        elements = 160 / mul;
        for (int i = 0; i < elements; i++) {
            array.add(i + 1);
        }
        Collections.shuffle(array);
        valCons = 3 * mul;
        xCons = 4 * mul;
    }

    @Override
    public boolean hasNext() {
        return !(sorted && finished);
    }

    int finish;

    void finish(Graphics graphics) {
        graphics.setColor(Color.GREEN);
        for (int i = 0; i < array.size(); i++) {
            int val = array.get(i) * valCons;
            if (i == finish) {
                graphics.setColor(Color.WHITE);
            }
            graphics.fillRect(i * xCons, height - val, valCons, val);
        }
        finish++;
        if (finish == array.size() + 1) finished = true;
    }

    @Override
    public void provideFrame(Graphics g) {

        g.setColor(Color.BLACK);
        g.fillRect(0, 0, width, height);
    }

    @Override
    public VideoSize getSize() {
        return new VideoSize(width, height, false);
    }
}
