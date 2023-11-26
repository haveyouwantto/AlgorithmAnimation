package hywt.algo.animation.sorting;

import java.awt.*;

public class CombSort extends Sort {

    int pointer = 0;
    int stop = (int) (elements / 1.3);
    int pointer2 = pointer + stop + 1;

    long compare = 0;
    boolean correct = true;


    public CombSort() {
        this(1);
    }

    public CombSort(int mul) {
        super(mul);
    }

    void step() {
        int current = array.statsGet(pointer);
        int next = array.statsGet(pointer2);
        compare++;
//        System.out.printf("%d %d \n", current,next);
        if (current > next) {
            array.statsSet(pointer, next);
            array.statsSet(pointer2, current);
            correct = false;
        }
        pointer++;
        pointer2 = pointer + stop + 1;
        if (pointer2 >= elements) {
            pointer = 0;
            stop /= 1.3;
            pointer2 = pointer + stop + 1;
            if (stop <= 1 && correct) sorted = true;
            correct = true;
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
        String info = String.format("Comb Sort - get = %d | set = %d | compare = %d", array.getGets(), array.getSets(), compare);
        g.drawChars(info.toCharArray(), 0, info.length(), 2, 10);
    }

    @Override
    protected void drawList(Graphics g) {

        for (int i = 0; i < array.size(); i++) {
            int val = array.get(i) * heightScale;
            if (i == pointer) {
                g.setColor(Color.RED);
            } else if (i == pointer2) {
                g.setColor(Color.GREEN);
            } else if (i == stop) {
                g.setColor(Color.YELLOW);
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
