package hywt.algo;

import hywt.algo.animation.Animations;
import hywt.algo.animation.DelayQuit;
import hywt.algo.animation.TextTransition;
import hywt.algo.animation.graph.BFS;
import hywt.algo.animation.graph.RecursiveSplitGenerator;
import hywt.algo.datatype.Graph;

import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        RecursiveSplitGenerator generator = new RecursiveSplitGenerator();
        Graph graph = generator.getGraph();
        BFS bfs = new BFS(graph);
        CreateAnimation.create(
                "-c:v hevc_nvenc -qp 16 -sws_flags neighbor -pix_fmt yuv420p -sws_flags neighbor -vf scale=-2:1080",
                new Animations(new DelayQuit(generator), new TextTransition(generator.getSize(), "开始搜索"), new DelayQuit(bfs)),
                new File("videos/gen.mkv")
        );
//        CreateAnimation.x264(
//                new Sudoku(),
//                new File("sudoku.mkv")
//        );
    }
}
