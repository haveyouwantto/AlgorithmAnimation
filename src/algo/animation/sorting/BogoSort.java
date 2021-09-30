package algo.animation.sorting;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Collections;

public class BogoSort extends Sort {

    int pointer = 0;
    long shuffles = 0;

    int tick = 0;

    public BogoSort(int mul) {
        super(mul);
        valCons = 12;
        xCons = 16;
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
    public BufferedImage provideFrame() {
        graphics.setColor(Color.BLACK);
        graphics.fillRect(0, 0, bim.getWidth(), bim.getHeight());
        if (sorted) {
            finish();
            return bim;
        }
        graphics.setColor(Color.WHITE);
        for (int i = 0; i < array.size(); i++) {
            int val = array.get(i) * valCons;
            graphics.fillRect(i * xCons, bim.getHeight() - val, valCons, val);
        }
        graphics.setColor(Color.WHITE);
        String info = String.format("Bogo Sort - shuffles = %d | %dx speed", shuffles, (int) (Math.pow(1.002, maxSkip)));
        graphics.drawChars(info.toCharArray(), 0, info.length(), 2, 10);
        if (tick > 60) {
            step();
        }
        tick++;
        return bim;
    }

    void finish() {
        super.finish();
        String info = String.format("Bogo Sort - shuffles = %d | %dx speed", shuffles, (int) (Math.pow(1.002, maxSkip)));
        graphics.drawChars(info.toCharArray(), 0, info.length(), 2, 10);
    }
}
