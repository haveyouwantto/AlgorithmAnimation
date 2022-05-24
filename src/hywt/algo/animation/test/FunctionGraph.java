package hywt.algo.animation.test;

import hywt.algo.animation.BasicAnimation;
import hywt.algo.datatype.VideoSize;

import java.awt.*;

public class FunctionGraph extends BasicAnimation {
    private int tick = 0;
    private double multiplier = 10;
    private int lpy;

    @Override
    public boolean hasNext() {
        return tick < 360;
    }

    @Override
    public VideoSize getSize() {
        return new VideoSize(500, 500);
    }

    @Override
    public void provideFrame(Graphics g) {
        super.provideFrame(g);
        double period1 = Math.sin(Math.toRadians(this.tick) + 0.3);
        double period2 = Math.sin(Math.toRadians(this.tick) + 1.2) * 5;
        double period3 = Math.sin(Math.toRadians(this.tick) + 1.6) * 10;

        for (int i = 0; i < 500; i++) {
            double x = (i - 250) / multiplier;
            double y = Math.sin(x * period1 + period2) * period3;
            int py = (int) (y * multiplier + 250);
            if (i > 1)
                g.drawLine(i - 1, lpy, i, py);
            lpy = py;
        }
        String str = String.format("f(x) = %.3f * sin(%.3fx + %.3f)", period3, period1, period2);
        g.drawChars(str.toCharArray(), 0, str.length(), 0, 10);
        tick++;
    }
}
