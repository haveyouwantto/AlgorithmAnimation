import algo.CreateAnimation;
import algo.animation.basic.AnimationTest;
import algo.animation.basic.FunctionGraph;

import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        CreateAnimation.nvenc(new FunctionGraph(), new File("func.mkv"));
    }
}
