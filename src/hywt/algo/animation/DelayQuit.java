package hywt.algo.animation;

import hywt.algo.datatype.VideoSize;

import java.awt.*;

public class DelayQuit implements AnimationGen {
    private AnimationGen base;
    boolean finished = false;
    long tick = 0;
    long finishTick = Long.MAX_VALUE;

    public DelayQuit(AnimationGen base) {
        this.base = base;
    }

    @Override
    public boolean hasNext() {
        return !(finished && tick - finishTick > 150);
    }

    @Override
    public VideoSize getSize() {
        return base.getSize();
    }

    @Override
    public void provideFrame(Graphics g) {
        if (base.hasNext()) {
            base.provideFrame(g);
        } else if (!finished) {
            finished = true;
            finishTick = tick;
        }
        tick++;
    }
}
