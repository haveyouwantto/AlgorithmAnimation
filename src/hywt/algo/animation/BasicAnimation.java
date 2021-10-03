package hywt.algo.animation;

import hywt.algo.datatype.VideoSize;

import java.awt.*;

public abstract class BasicAnimation implements AnimationGen {
    @Override
    public void provideFrame(Graphics g) {
        VideoSize size = getSize();
        if (size.flip) {
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, size.width, size.height);
            g.setColor(Color.WHITE);
        }
    }
}
