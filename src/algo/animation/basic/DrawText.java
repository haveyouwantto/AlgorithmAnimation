package algo.animation.basic;

import algo.animation.AnimationGen;
import algo.animation.BasicAnimation;
import algo.datatype.VideoSize;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class DrawText extends BasicAnimation {

    int ord;

    @Override
    public boolean hasNext() {
        return ord <= 0xffff;
    }

    @Override
    public VideoSize getSize() {
        return new VideoSize(320, 280, true);
    }

    @Override
    public void provideFrame(Graphics g) {
        char[] hex = "0123456789ABCDEF".toCharArray();
        for (int x = 0; x < 16; x++) {
            g.drawChars(hex, x, 1, x * 16 + 64, 16);
        }
        for (int y = 0; y < 16; y++) {
            char[] num = String.format("%04X", ord + y * 16).toCharArray();
            g.drawChars(num, 0, num.length, 16, y * 16 + 32);
            for (int x = 0; x < 16; x++) {
                g.drawChars(new char[]{(char) (ord + y * 16 + x)}, 0, 1, x * 16 + 64, y * 16 + 32);
            }
        }
        ord += 256;
    }
}
