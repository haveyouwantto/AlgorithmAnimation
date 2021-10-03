package hywt.algo.animation.sorting;

import hywt.algo.animation.AnimationGen;
import hywt.algo.datatype.StatsArrayList;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Collections;

public abstract class Sort implements AnimationGen {
    StatsArrayList<Integer> array = new StatsArrayList<>();
    int elements;
    int mul;
    int valCons;
    int xCons;
    BufferedImage bim = new BufferedImage(640, 480, BufferedImage.TYPE_INT_RGB);
    Graphics graphics = bim.getGraphics();
    boolean sorted;
    boolean finished;

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

    void finish() {
        graphics.setColor(Color.GREEN);
        for (int i = 0; i < array.size(); i++) {
            int val = array.get(i) * valCons;
            if (i == finish) {
                graphics.setColor(Color.WHITE);
            }
            graphics.fillRect(i * xCons, bim.getHeight() - val, valCons, val);
        }
        finish++;
        if (finish == array.size() + 1) finished = true;
    }
}
