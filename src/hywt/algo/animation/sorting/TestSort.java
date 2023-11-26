package hywt.algo.animation.sorting;

import java.awt.*;
import java.util.Random;

public class TestSort extends Sort {

    int pointer = 0;
    int pointer2 = 0;
    Random r = new Random();

    long compare = 0;

    boolean correct = true;

    public TestSort() {
        this(1);
    }

    public TestSort(int mul) {
        super(mul);
    }

    void step() {
        pointer = r.nextInt(elements);
        pointer2 = r.nextInt(elements);
        if (pointer2 < pointer) {
            int temp = pointer;
            pointer = pointer2;
            pointer2 = temp;
        }
        int current = array.statsGet(pointer);
        int next = array.statsGet(pointer2);
        compare++;
        if (current > next) {
            correct = false;
            array.statsSet(pointer, next);
            array.statsSet(pointer2, current);
        }
    }

    @Override
    public void provideFrame(Graphics g) {
        super.provideFrame(g);
        if (tick > 60 && !sorted) {
            step();
        }
        tick++;
    }

    @Override
    public void drawText(Graphics g) {
        g.setColor(Color.WHITE);
        String info = String.format("Test Sort - get = %d | set = %d | compare = %d", array.getGets(), array.getSets(), compare);
        g.drawChars(info.toCharArray(), 0, info.length(), 2, 10);
    }

    @Override
    protected void drawList(Graphics g) {

        for (int i = 0; i < array.size(); i++) {
            int val = array.get(i) * heightScale;
            if (i == pointer) {
                g.setColor(Color.RED);
            } else {
                g.setColor(Color.WHITE);
            }
            g.fillRect(i * widthScale, height - val, mul < 0 ? 2 : heightScale, val);
        }
    }

    @Override
    void finish(Graphics g) {
        super.finish(g);
        drawText(g);
    }
}
