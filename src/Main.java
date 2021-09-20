import algo.CreateAnimation;
import algo.animation.BubbleSort;
import algo.animation.basic.AnimationTest;
import algo.animation.basic.FunctionGraph;

import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        CreateAnimation.create("-c:v h264_nvenc -qp 16 -vf scale=-2:2160 -sws_flags neighbor -pix_fmt yuv420p", new BubbleSort(), new File("func.mkv"));
    }
}
