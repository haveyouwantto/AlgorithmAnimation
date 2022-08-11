package hywt.algo;

import hywt.algo.animation.AnimationGen;
import hywt.algo.animation.DelayQuit;
import hywt.algo.animation.dist.NormalDistribution;
import hywt.algo.animation.sorting.QuickSort;
import hywt.algo.animation.test.FFT;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class Main {
    public static void main(String[] args) throws IOException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        AnimationGen anim = new FFT();
        CreateAnimation.create(
                "-c:v hevc_nvenc -qp 16 -sws_flags neighbor -pix_fmt yuv420p -sws_flags neighbor",
                new DelayQuit(anim),
                new File("videos/fft2.mkv")
        );
//        CreateAnimation.nvenc(
//                anim,
//                new File("videos","thankyou.mkv")
//        );
//        CreateAnimation.x264(anim, new File("videos/thankyou2.mkv"));
//        CreateAnimation.png(anim, new File("videos/"));
    }
}
