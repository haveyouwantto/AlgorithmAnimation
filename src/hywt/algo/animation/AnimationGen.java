package hywt.algo.animation;

import hywt.algo.datatype.VideoSize;

import java.awt.*;

public interface AnimationGen {
    boolean hasNext();

    VideoSize getSize();

    void provideFrame(Graphics g);
}
