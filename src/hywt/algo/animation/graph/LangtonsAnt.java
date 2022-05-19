package hywt.algo.animation.graph;

import hywt.algo.datatype.Direction;
import hywt.algo.datatype.Graph;

import java.awt.*;
import java.io.IOException;

public class LangtonsAnt extends BasicGraph {
    private Point pos;
    private Direction direction;

    public LangtonsAnt() throws IOException {
        super();
        graph = new Graph(80, 80);
        setMul(8);
        pos = new Point(graph.getWidth() / 2, graph.getHeight() / 2);
        direction = Direction.NORTH;
    }

    @Override
    public boolean hasNext() {
        return super.hasNext();
    }

    int gen = 0;

    @Override
    public void provideFrame(Graphics g) {
        super.provideFrame(g);
        int mul = getMul();
        int offsetX = 320 - (graph.getWidth() * mul / 2);
        int offsetY = 240 - (graph.getHeight() * mul / 2);
        int x = pos.x * mul + offsetX, y = pos.y * mul + offsetY;
        g.setColor(Color.RED);

        switch (direction) {
            case NORTH:
            case SOUTH:
                g.fillRect(x + mul / 2 - 1, y, 3, mul);
                break;
            case EAST:
            case WEST:
                g.fillRect(x, y + mul / 2 - 1, mul, 3);
                break;
        }

        g.setColor(Color.WHITE);
        g.drawString(String.format("兰顿蚂蚁 - generations = %d", gen), 4, 16);
        if (tick > 150) {
            switch (graph.get(pos)) {
                case 0:
                    graph.set(pos, 1);
                    direction = direction.right();
                    pos = direction.move(pos);
                    break;
                case 1:
                    graph.set(pos, 0);
                    direction = direction.left();
                    pos = direction.move(pos);
                    break;
            }
            gen++;
        }
    }
}
