package hywt.algo.animation.sorting;

import java.awt.*;
import java.util.Collections;

public class BogoSort extends Sort {

    int pointer = 0;
    long shuffles = 0;

    int tick = 0;

    public BogoSort() {
        this(16);
    }

    public BogoSort(int mul) {
        super(mul);
        if (mul > 1) {
            heightScale = 12;
            widthScale = 16;
        }
    }

    int maxSkip = 1;

    void step() {
        int skip = 0;
        boolean fail = false;
        while (skip < (int) (Math.pow(1.002, maxSkip))) {
            for (int i = 0; i < elements - 1; i++) {
                int next = array.statsGet(i + 1);
                int current = array.statsGet(i);
                if (next < current) {
                    shuffles++;
                    fail = true;
                    Collections.shuffle(array);
                    break;
                }
            }
            skip++;
        }
        maxSkip++;
        sorted = !fail;
    }

    @Override
    public void provideFrame(Graphics g) {
        super.provideFrame(g);
        if (tick > 60) {
            step();
        }
        tick++;
    }

    @Override
    public void drawText(Graphics g) {
        g.setColor(Color.WHITE);
        String info = String.format("Bogo Sort - shuffles = %d | %dx speed", shuffles, (int) (Math.pow(1.002, maxSkip)));
        g.drawChars(info.toCharArray(), 0, info.length(), 2, 10);
    }

    @Override
    protected void drawList(Graphics g) {
        g.setColor(Color.WHITE);
        for (int i = 0; i < array.size(); i++) {
            int val = array.get(i) * heightScale;
            g.fillRect(i * widthScale, height - val, mul < 0 ? 2 : heightScale, val);
        }
    }

    void finish(Graphics g) {
        super.finish(g);
        drawText(g);
    }
}
