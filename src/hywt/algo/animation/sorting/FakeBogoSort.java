package hywt.algo.animation.sorting;

import java.awt.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Collections;

public class FakeBogoSort extends Sort {

    int pointer = 0;
    double shuffles = 0;

    int tick = 0;

    public FakeBogoSort() {
        this(1);
    }

    public FakeBogoSort(int mul) {
        super(mul);
    }

    double maxSkip = 4.71472364E284 * 1.47;
    double step = 1;
    double mul = 1.00001;

    void step() {
        if (shuffles < maxSkip) {
            Collections.shuffle(array);
            shuffles += step;
            step *= mul;
            mul *= 1.0001;
        } else {
            Collections.sort(array);
            sorted = true;
        }
        maxSkip++;
    }

    @Override
    public void provideFrame(Graphics g) {
        super.provideFrame(g);
        if (tick > 60) {
            step();
        }
        tick++;
    }

    private String formatNumber(double number) {
        NumberFormat formatter = new DecimalFormat("0.00000E0");

        if (number < 9007199254740992.0) {
            formatter = new DecimalFormat("###0");
        }

        String formattedNumber = formatter.format(number);
        return formattedNumber;
    }

    @Override
    public void drawText(Graphics g) {
        g.setColor(Color.WHITE);

        String info = String.format("Bogo Sort - shuffles = %s | %sx speed", formatNumber(shuffles), formatNumber(step));
        g.drawChars(info.toCharArray(), 0, info.length(), 2, 10);
    }

    @Override
    protected void drawList(Graphics g) {
        g.setColor(new Color(32, 32, 32));
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
