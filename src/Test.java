import hywt.algo.CreateAnimation;
import hywt.algo.animation.AnimationGen;
import hywt.algo.animation.Animations;
import hywt.algo.animation.TextTransition;
import hywt.algo.animation.general.Hanoi;
import hywt.algo.animation.general.Sudoku;
import hywt.algo.animation.sorting.MergeSort;
import hywt.algo.animation.sorting.QuickSort;
import hywt.algo.gui.GUI;

import java.io.File;
import java.io.IOException;

public class Test {
    public static void main(String[] args) throws IOException {
        GUI gui = new GUI(new Hanoi());
        gui.setVisible(true);
    }
}
