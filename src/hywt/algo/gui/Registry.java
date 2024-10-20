package hywt.algo.gui;

import hywt.algo.animation.AnimationGen;
import hywt.algo.animation.dist.NormalDistribution;
import hywt.algo.animation.general.Hanoi;
import hywt.algo.animation.general.MonetPi;
import hywt.algo.animation.general.Sudoku;
import hywt.algo.animation.graph.*;
import hywt.algo.animation.scatter.KMeans;
import hywt.algo.animation.sorting.*;
import hywt.algo.animation.test.FFT;

import java.util.*;

public class Registry {
    protected static Map<String, Set<Class<? extends AnimationGen>>> registry;

    static {
        registry = new LinkedHashMap<>();

        registry.put("sort", new LinkedHashSet<>());
        registry.get("sort").addAll(
                List.of(
                        BubbleSort.class,
                        SelectionSort.class,
                        QuickSort.class,
                        MergeSort.class,
                        RadixSort.class,
                        StoogeSort.class,
                        SlowSort.class,
                        BogoSort.class,
                        InsertionSort.class,
                        CombSort.class,
                        ShellSort.class
                )
        );

        registry.put("generic", new LinkedHashSet<>());
        registry.get("generic").addAll(
                List.of(
                        Hanoi.class,
                        Sudoku.class,
                        MonetPi.class,
                        FFT.class
                )
        );

        registry.put("graph", new LinkedHashSet<>());
        registry.get("graph").addAll(
                List.of(
                        BFS.class,
                        RecursiveSplitGenerator.class,
                        DFS.class,
                        BestFS.class,
                        LangtonsAnt.class
                )
        );

        registry.put("dist",new LinkedHashSet<>());
        registry.get("dist").addAll(
                List.of(
                        NormalDistribution.class,
                        KMeans.class
                )
        );

    }
}
