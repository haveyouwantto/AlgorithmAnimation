package hywt.math.graph;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import hywt.math.number.MyComplex;

public class FractalGraph {
    public static final int[] PALETTE = { 0xf44336, 0xE91E63, 0x9C27B0, 0x673AB7, 0x3F51B5, 0x2196F3, 0x03A9F4,
            0x00BCD4, 0x009688, 0x4CAF50, 0x8BC34A, 0xCDDC39, 0xFFEB3B, 0xFFC107, 0xFF9800, 0xFF5722 };

    private int max;
    private double magn;

    public FractalGraph() {
        this(1000, 1);
    }

    public FractalGraph(int max) {
        this(max, 1);
    }

    public FractalGraph(int max, double magn) {
        this.max = max;
        this.magn = magn;
    }

    public BufferedImage draw(BufferedImage image, MyComplex oz) {
        int width = image.getWidth(), height = image.getHeight();
        double widthPx = 4d / magn / width, heightPx = 4d / magn / height;
        for (int y = 0; y < height; y++) {
            double im = (y - height / 2) * heightPx + oz.imag;
            for (int x = 0; x < width; x++) {
                double re = (x - width / 2) * widthPx + oz.real;

                MyComplex c = new MyComplex(re, im);
                MyComplex z = new MyComplex(re, im);
                int iter = 0;
                while (z.length() < 2 && iter < max) {
                    z = z.multiply(z).add(c); // z = z^2 + c
                    iter++;
                }
                if (iter == max)
                    image.setRGB(x, y, 0);
                else
                    image.setRGB(x, y, PALETTE[iter % PALETTE.length] ^ iter);
            }
        }
        return image;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        if (max >= 1)
            this.max = max;
        else
            throw new IllegalArgumentException();
    }

    public double getMagn() {
        return magn;
    }

    public void setMagn(double magn) {
        this.magn = magn;
    }
}