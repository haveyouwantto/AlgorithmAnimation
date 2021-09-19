package algo.animation.basic;

import algo.animation.AnimationGen;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class AnimationTest implements AnimationGen {
    private int i = 0;


    @Override
    public boolean hasNext() {
        return i < 360;
    }

    @Override
    public BufferedImage provideFrame() {
        BufferedImage bim = new BufferedImage(640, 480, BufferedImage.TYPE_INT_RGB);
        Graphics graphics = bim.getGraphics();
        int i2 = (int) ((Math.sin(Math.toRadians(i)) + 1) * 180);
        i++;
        graphics.drawOval(10, 10, i2, i2);
        String str = String.format("frame=%3d  radius=%3d", i, i2);
        graphics.drawChars(str.toCharArray(), 0, str.length(), 100, 100);
        return bim;
    }
}
