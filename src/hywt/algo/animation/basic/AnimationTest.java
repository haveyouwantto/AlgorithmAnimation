package hywt.algo.animation.basic;

import hywt.algo.animation.BasicAnimation;
import hywt.algo.datatype.VideoSize;

import java.awt.*;

public class AnimationTest extends BasicAnimation {
    private int i = 0;


    @Override
    public boolean hasNext() {
        return i < 360;
    }

    @Override
    public VideoSize getSize() {
        return new VideoSize(640,480);
    }

    @Override
    public void provideFrame(Graphics g) {
        super.provideFrame(g);
        int i2 = (int) ((Math.sin(Math.toRadians(i)) + 1) * 180);
        i++;
        g.drawOval(10, 10, i2, i2);
        String str = String.format("frame=%3d  diameter=%3d", i, i2);
        g.drawChars(str.toCharArray(), 0, str.length(), 100, 100);
    }
}
