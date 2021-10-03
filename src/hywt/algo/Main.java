package hywt.algo;

import hywt.algo.CreateAnimation;
import hywt.algo.animation.basic.DrawText;

import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        CreateAnimation.create(
                "-c:v hevc_nvenc -qp 16 -sws_flags neighbor -pix_fmt yuv420p -sws_flags neighbor -vf scale=-2:1080",
                new DrawText(),
                new File("chars.mkv")
        );
    }
}
