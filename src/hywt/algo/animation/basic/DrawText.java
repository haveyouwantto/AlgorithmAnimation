package hywt.algo.animation.basic;

import hywt.algo.animation.AnimationGen;

import java.awt.*;
import java.awt.image.BufferedImage;

public class DrawText implements AnimationGen {

    int ord;

    @Override
    public boolean hasNext() {
        return ord <= 0xffff;
    }

    @Override
    public BufferedImage provideFrame() {
        BufferedImage bim = new BufferedImage(320, 280, BufferedImage.TYPE_INT_RGB);
        Graphics g = bim.getGraphics();
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
        return bim;
    }
}
