package hywt.algo.animation.graph;

import hywt.algo.datatype.Graph;

import java.io.IOException;

public class EmptyGraph extends BasicGraph {
    public EmptyGraph() throws IOException {
        int width = 81;
        int height = 81;
        graph = new Graph(width, height);
        graph.drawHorizontal(0, width - 1, 0, Graph.WALL);
        graph.drawHorizontal(0, width - 1, height - 1, Graph.WALL);
        graph.drawVertical(0, 0, height - 1, Graph.WALL);
        graph.drawVertical(width - 1, 0, height - 1, Graph.WALL);
    }
}
