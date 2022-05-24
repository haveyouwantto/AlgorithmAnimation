package hywt.algo;

import hywt.algo.animation.AnimationGen;
import hywt.algo.animation.DelayQuit;
import hywt.algo.animation.dist.NormalDistribution;
import hywt.algo.animation.dist.PowerDistribution;
import hywt.algo.animation.graph.*;
import hywt.algo.animation.sorting.BubbleSort;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class Main {
    public static void main(String[] args) throws IOException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        AnimationGen anim = new BubbleSort();
        CreateAnimation.create(
                "-c:v hevc_nvenc -qp 16 -sws_flags neighbor -pix_fmt yuv420p -sws_flags neighbor -vf scale=-2:2160",
                new DelayQuit(anim),
                new File("videos/1.mkv")
        );
//        CreateAnimation.av1(
//                anim,
//                new File("videos/1.mkv")
//        );
    }
}
