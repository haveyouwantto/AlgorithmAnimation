package hywt.algo.animation.general;

import hywt.algo.animation.AnimationGen;
import hywt.algo.datatype.VideoSize;
import hywt.math.function.Function;
import hywt.math.function.Mapper;

import java.awt.*;
import java.util.Random;

public class MonetPi implements AnimationGen {
    public final Function mapperX;
    public final Function mapperY;
    public final Function actual;
    Random r;
    int tick;
    long skip;

    long points;
    long inside;

    public MonetPi() {
        mapperX = new Mapper(-1, 1, 320 - 150, 320 + 150);
        mapperY = new Mapper(-1, 1, 240 - 150, 240 + 150);
        actual = new Mapper(0, 1, -1, 1);
        r = new Random();
        tick = 1;

        points = 0;
        inside = 0;
        skip = 1;
    }

    @Override
    public boolean hasNext() {
        return tick <= 3720;
    }

    @Override
    public VideoSize getSize() {
        return new VideoSize(640, 480);
    }

    @Override
    public void provideFrame(Graphics g) {
        if (tick == 1) {
            g.setColor(Color.WHITE);
            g.drawRect(320 - 150, 240 - 150, 300, 300);
            g.drawOval(320 - 150, 240 - 150, 300, 300);
        }
        Color in = Color.getHSBColor(tick / 1000f, 1, 1);
        Color out = Color.getHSBColor(0, 0, (float) (Math.sin(tick / 100f) / 2) + 0.5f);
        if (tick > 150) {
            skip = (long) (Math.pow(1.004, tick));
            for (long i = 0; i < skip; i++) {
                double x = actual.get(r.nextDouble());
                double y = actual.get(r.nextDouble());
                points++;
                if (x * x + y * y < 1) {
                    inside++;
                    g.setColor(in);
                } else {
                    g.setColor(out);
                }
                if (i < 100000) {
                    g.drawRect((int) mapperX.get(x), (int) mapperY.get(y), 0, 0);
                }
            }
        }
        tick++;

        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getSize().width, 32);
        g.setColor(Color.WHITE);
        g.drawString(String.format("蒙特卡罗模拟与圆周率 - points = %d | inside = %d | speed=%dx | ratio = %s", points, inside, skip, inside * 1d / points * 4), 0, 16);
    }
}
