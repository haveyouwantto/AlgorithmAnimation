package hywt.algo.animation;

import java.awt.image.BufferedImage;

public interface AnimationGen {
    boolean hasNext();
    BufferedImage provideFrame();
}
