package hywt.algo.animation.graph;

import hywt.algo.animation.BasicAnimation;
import hywt.algo.datatype.Graph;
import hywt.algo.datatype.VideoSize;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.function.Function;

public class BasicGraph extends BasicAnimation {
    protected Graph graph;
    protected Color explored;
    protected HashMap<Integer, Function<Point, Color>> colorMap;
    int tick;
    private int mul;

    public BasicGraph() throws IOException {
        this(Graph.loadImage(new File("medium-maze.png")));
    }

    public BasicGraph(Graph graph) {
        this.graph = graph;
        explored = new Color(0x3D85FF);
        mul = 4;
        colorMap = new HashMap<>();
        registerColorMapper(Graph.WALL, point -> Color.WHITE);
        registerColorMapper(Graph.START, point -> Color.RED);
        registerColorMapper(Graph.END, point -> Color.GREEN);
        registerColorMapper(Graph.EXPLORED, point -> explored);
        registerColorMapper(Graph.PATH, point -> Color.YELLOW);
        registerColorMapper(Graph.TRIED, point -> Color.GRAY);
    }

    @Override
    public boolean hasNext() {
        return true;
    }

    @Override
    public VideoSize getSize() {
        return new VideoSize(640, 480);
    }

    public void registerColorMapper(int state, Function<Point, Color> function) {
        colorMap.put(state, function);
    }

    @Override
    public void provideFrame(Graphics g) {
        super.provideFrame(g);
        int offsetX = 320 - (graph.getWidth() * mul / 2);
        int offsetY = 240 - (graph.getHeight() * mul / 2);
        for (int y = 0; y < graph.getHeight(); y++) {
            for (int x = 0; x < graph.getWidth(); x++) {
                if (graph.get(x, y) == Graph.EMPTY) continue;
                g.setColor(colorMap.get(graph.get(x, y)).apply(new Point(x, y)));
                g.fillRect(x * mul + offsetX, y * mul + offsetY, mul, mul);
            }
        }
        tick++;
    }

    public int getMul() {
        return mul;
    }

    public void setMul(int mul) {
        this.mul = mul;
    }
}
