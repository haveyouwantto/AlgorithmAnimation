package hywt.algo.animation.graph;

import hywt.algo.animation.BasicAnimation;
import hywt.algo.datatype.Graph;
import hywt.algo.datatype.VideoSize;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public class BasicGraph extends BasicAnimation {
    protected Graph graph;
    private Color explored;
    int tick;
    int mul;

    public BasicGraph() throws IOException {
        graph = Graph.loadImage(new File("medium-maze.png"));
        explored = new Color(0x3D85FF);
        mul = 4;
    }

    @Override
    public boolean hasNext() {
        return true;
    }

    @Override
    public VideoSize getSize() {
        return new VideoSize(640, 480);
    }

    @Override
    public void provideFrame(Graphics g) {
        super.provideFrame(g);
        int offsetX = 320 - (graph.getWidth() * mul / 2);
        int offsetY = 240 - (graph.getHeight() * mul / 2);
        for (int y = 0; y < graph.getHeight(); y++) {
            for (int x = 0; x < graph.getWidth(); x++) {
                switch (graph.get(x, y)) {
                    case Graph.EMPTY:
                        continue;
                    case Graph.WALL:
                        g.setColor(Color.WHITE);
                        break;
                    case Graph.START:
                        g.setColor(Color.RED);
                        break;
                    case Graph.END:
                        g.setColor(Color.GREEN);
                        break;
                    case Graph.EXPLORED:
                        g.setColor(explored);
                        break;
                    case Graph.PATH:
                        g.setColor(Color.YELLOW);
                        break;
                }
                g.fillRect(x * mul + offsetX, y * mul + offsetY, mul, mul);
            }
        }
        tick++;
    }
}
