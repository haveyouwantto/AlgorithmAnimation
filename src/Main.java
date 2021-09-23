import algo.CreateAnimation;
import algo.animation.sorting.*;

import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        CreateAnimation.create(
                "-c:v hevc_nvenc -qp 16 -sws_flags neighbor -pix_fmt yuv420p",
                new RadixSort(1),
                new File("bogo.mkv")
        );
    }
}
