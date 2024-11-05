package hywt.algo;

import java.awt.*;

public class GraphUtils {
    public static void drawText(Graphics g, String text, int x, int y) {
        g.drawChars(text.toCharArray(), 0, text.length(), x, y);
    }

    public static String intToAlphabet(int num) {
        StringBuilder result = new StringBuilder();

        while (num >= 0) {
            int remainder = num % 26;
            result.insert(0, (char) ('a' + remainder));
            num = num / 26 - 1; // Adjust the number for the next iteration
        }

        return result.toString();
    }
}
