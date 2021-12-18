package hywt.algo;

import hywt.algo.animation.etc.AllSortings;
import hywt.algo.animation.general.Hanoi;
import hywt.algo.animation.general.Sudoku;
import hywt.algo.animation.graph.BFS;

import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        CreateAnimation.create(
                "-c:v hevc_nvenc -qp 16 -sws_flags neighbor -pix_fmt yuv420p -sws_flags neighbor -vf scale=-2:2160",
                new BFS(),
                new File("bfs.mkv")
        );
//        CreateAnimation.x264(
//                new Sudoku(),
//                new File("sudoku.mkv")
//        );
    }
}
