package hywt.algo;

import hywt.algo.animation.AnimationGen;
import hywt.algo.animation.DelayQuit;
import hywt.algo.animation.dist.NormalDistribution;
import hywt.algo.animation.sorting.QuickSort;
import hywt.algo.animation.sorting.RadixSort;
import hywt.algo.animation.test.FFT;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class Main {
    public static void main(String[] args) throws IOException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        
        // Replace with the name of the AnimationGen class you want to use.
        AnimationGen anim = new RadixSort();

        // Comment this if you don't have an NVIDIA graphics card
        CreateAnimation.create(
                "-c:v hevc_nvenc -qp 16 -pix_fmt yuv420p -sws_flags neighbor -vf scale=-2:2160",
                new DelayQuit(anim),
                new File("videos/radixsortv2.mkv")
        );
        //        CreateAnimation.nvenc(
        //                anim,
        //                new File("videos","thankyou.mkv")
        //        );

        // Uncomment this if you don't have an NVIDIA graphics card
        //        CreateAnimation.x264(anim, new File("videos/thankyou2.mkv"));
        //        CreateAnimation.png(anim, new File("videos/"));
    }
}
