package hywt.algo.process;

import java.awt.image.BufferedImage;

public class Etc {
    public static byte[] toByteArray(BufferedImage bim) {
        int x = bim.getWidth();
        int size = x * bim.getHeight();
        byte[] out = new byte[size * 3];
        for (int i = 0; i < size; i++) {
            int data = bim.getRGB(i % x, i / x);
            out[i * 3] = (byte) ((data >> 16) & 0xff);
            out[i * 3 + 1] = (byte) ((data >> 8) & 0xff);
            out[i * 3 + 2] = (byte) (data & 0xff);
        }
        return out;
    }
}
