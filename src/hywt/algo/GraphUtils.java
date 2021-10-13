package hywt.algo;

import java.awt.*;

public class GraphUtils {
    public static void drawText(Graphics g, String text, int x, int y) {
        g.drawChars(text.toCharArray(), 0, text.length(), x, y);
    }
}
