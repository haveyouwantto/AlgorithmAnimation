package hywt.algo.animation.etc;

import hywt.algo.animation.AnimationGen;
import hywt.algo.animation.Animations;
import hywt.algo.animation.TextTransition;
import hywt.algo.animation.sorting.*;
import hywt.algo.datatype.VideoSize;

import java.awt.*;

public class AllSortings implements AnimationGen {
    private AnimationGen gen;

    public AllSortings() {
        AnimationGen gen1 = new BubbleSort(4);
        AnimationGen trans1 = new TextTransition(gen1.getSize(), "冒泡排序");

        AnimationGen trans2 = new TextTransition(gen1.getSize(), "选择排序");
        AnimationGen gen2 = new SelectionSort(4);
        AnimationGen trans3 = new TextTransition(gen1.getSize(), "插入排序");
        AnimationGen gen3 = new InsertionSort(4);
        AnimationGen trans4 = new TextTransition(gen1.getSize(), "快速排序");
        AnimationGen gen4 = new QuickSort(2);
        AnimationGen trans5 = new TextTransition(gen1.getSize(), "归并排序");
        AnimationGen gen5 = new MergeSort(2);
        AnimationGen trans6 = new TextTransition(gen1.getSize(), "基数排序");
        AnimationGen gen6 = new RadixSort(2);
        AnimationGen trans7 = new TextTransition(gen1.getSize(), "臭皮匠排序");
        AnimationGen gen7 = new StoogeSort(4);
        AnimationGen trans8 = new TextTransition(gen1.getSize(), "慢速排序");
        AnimationGen gen8 = new SlowSort(4);
        gen = new Animations(trans1, gen1, trans2, gen2, trans3, gen3, trans4, gen4, trans5, gen5, trans6, gen6, trans7, gen7, trans8, gen8);
    }

    @Override
    public boolean hasNext() {
        return gen.hasNext();
    }

    @Override
    public VideoSize getSize() {
        return gen.getSize();
    }

    @Override
    public void provideFrame(Graphics g) {
        gen.provideFrame(g);
    }
}
