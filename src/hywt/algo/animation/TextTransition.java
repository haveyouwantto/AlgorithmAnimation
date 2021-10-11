package hywt.algo.animation;

import hywt.algo.datatype.VideoSize;

import java.awt.*;

public class TextTransition implements AnimationGen {
    private int tick;
    private final VideoSize size;
    private final char[] string;

    public TextTransition(VideoSize size, String text) {
        this.size = size;
        string = text.toCharArray();
    }

    @Override
    public boolean hasNext() {
        return tick < 120;
    }

    @Override
    public VideoSize getSize() {
        return size;
    }

    @Override
    public void provideFrame(Graphics g) {
        if (tick == 0) {
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, size.width, size.height);
            g.setColor(Color.WHITE);
            g.setFont(g.getFont().deriveFont(32f));
            FontMetrics metrics = g.getFontMetrics();
            g.drawChars(
                    string,
                    0,
                    string.length,
                    size.width / 2 - metrics.charsWidth(string, 0, string.length) / 2,
                    size.height / 2
            );
        }
        tick++;
    }
}
