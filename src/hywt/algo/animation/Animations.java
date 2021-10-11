package hywt.algo.animation;

import hywt.algo.datatype.VideoSize;

import java.awt.*;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.LinkedList;

public class Animations implements AnimationGen {

    private final LinkedList<AnimationGen> animations;
    private VideoSize size;
    private boolean next;
    private AnimationGen gen;

    private boolean first;
    private Font defFont;
    private Color defColor;

    public Animations(AnimationGen... animationGens) {
        for (int i = 0; i < animationGens.length; i++) {
            AnimationGen e = animationGens[i];
            if (i == 0) {
                size = e.getSize();
            } else if (!e.getSize().equals(size))
                throw new InputMismatchException(String.format("VideoSize %s != %s", e.getSize(), size));
        }
        this.animations = new LinkedList<>(Arrays.asList(animationGens));
        this.next = true;
        this.first = true;
    }

    @Override
    public boolean hasNext() {
        return next;
    }

    @Override
    public VideoSize getSize() {
        return size;
    }

    @Override
    public void provideFrame(Graphics g) {
        if (first) {
            defFont = g.getFont();
            defColor = g.getColor();
            first = false;
        }
        if (gen != null && gen.hasNext()) {
            gen.provideFrame(g);
        } else {
            if (animations.isEmpty()) {
                next = false;
            } else {
                gen = animations.removeFirst();
                g.setFont(defFont);
                g.setColor(defColor);
            }
        }
    }
}
