package hywt.algo.animation.sorting;

import hywt.algo.animation.AnimationGen;
import hywt.algo.datatype.StatsArrayList;
import hywt.algo.datatype.VideoSize;

import java.awt.*;
import java.util.Collections;

public abstract class Sort implements AnimationGen {
    StatsArrayList<Integer> array = new StatsArrayList<>();
    int elements;
    int mul;
    int heightScale;
    int widthScale;
    boolean sorted;
    boolean finished;

    int tick = 0;
    int width = 640;
    int height = 480;

    public Sort() {
        this(1);
    }
    public Sort(int mul) {
        this.mul = mul;
        if (mul > 0) {
            if (160 % mul != 0) throw new ArithmeticException("mul must be dividable by 160");
            elements = 160 / mul;
            heightScale = 3 * mul;
            widthScale = 4 * mul;
        } else {
            elements = -mul;
            heightScale = 1;
            widthScale = 2;
            width = -mul * 2;
            height = -mul;
        }
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

    void finish(Graphics graphics) {
        graphics.setColor(Color.GREEN);
        for (int i = 0; i < array.size(); i++) {
            int val = array.get(i) * heightScale;
            if (i == finish) {
                graphics.setColor(Color.WHITE);
            }
            graphics.fillRect(i * widthScale, height - val,  mul < 0? 2 : heightScale, val);
        }
        drawText(graphics);
        finish++;
        if (finish == array.size() + 1) finished = true;
    }

    @Override
    public void provideFrame(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, width, height);
        if (sorted) {
            finish(g);
            return;
        }
        drawList(g);
        drawText(g);
    }

    @Override
    public VideoSize getSize() {
        return new VideoSize(width, height);
    }

    protected abstract void drawText(Graphics g);
    protected abstract void drawList(Graphics g);
}
