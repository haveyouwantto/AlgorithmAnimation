package hywt.algo.animation.graph;

import hywt.algo.animation.AnimationGen;
import hywt.algo.animation.Animations;
import hywt.algo.animation.BasicAnimation;
import hywt.algo.animation.TextTransition;
import hywt.algo.datatype.VideoSize;

import java.awt.*;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class GenAndSearch extends BasicAnimation {
    private AnimationGen animations;

    public GenAndSearch() throws IOException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        this(RecursiveSplitGenerator.class, BFS.class);
    }

    public GenAndSearch(Class<? extends BasicGraph> genClass, Class<? extends BasicGraph> searchClass) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        BasicGraph gen = genClass.getConstructor().newInstance();
        BasicGraph search = searchClass.getConstructor().newInstance();
        search.setGraph(gen.graph);
        search.setMul(gen.getMul());
        this.animations = new Animations(gen, new TextTransition(gen.getSize(), "开始搜索"), search);
    }

    @Override
    public boolean hasNext() {
        return animations.hasNext();
    }

    @Override
    public VideoSize getSize() {
        return animations.getSize();
    }

    @Override
    public void provideFrame(Graphics g) {
        animations.provideFrame(g);
    }
}
