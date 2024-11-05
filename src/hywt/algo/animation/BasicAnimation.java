package hywt.algo.animation;

import hywt.algo.datatype.VideoSize;

import java.awt.*;

public abstract class BasicAnimation implements AnimationGen {
    private boolean flip = true;

    public void setFlip(boolean flip) {
        this.flip = flip;
    }

    @Override
    public void provideFrame(Graphics g) {
        if (flip) {
            VideoSize size = getSize();
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, size.width, size.height);
            g.setColor(Color.WHITE);
        }
    }
}
