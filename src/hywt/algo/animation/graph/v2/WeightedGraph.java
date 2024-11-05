package hywt.algo.animation.graph.v2;

import hywt.algo.GraphUtils;
import hywt.algo.animation.BasicAnimation;
import hywt.algo.datatype.VideoSize;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.List;
import java.util.*;

public class WeightedGraph extends BasicAnimation {
    protected List<Node> nodes;

    public WeightedGraph() {
        nodes = new ArrayList<>();
        Random r = new Random();
        VideoSize size = getSize();

        int i = 0;
        int spacing = 64;

        List<List<Node>> nodes = new ArrayList<>();

        for (int y = 0; y < (double) size.height / spacing / 3 * 2; y++) {
            List<Node> nodes1 = new ArrayList<>();
            nodes.add(nodes1);
            for (int x = 0; x < (double) size.width / spacing / 3 * 2; x++) {
                int screenX = x * 64 + size.width / 6;
                int screenY = y * 64 + size.height / 6;
                nodes1.add(new Node(screenX, screenY, GraphUtils.intToAlphabet(i++)));
                connect(nodes, x, y, x - 1, y, r.nextInt(20));
                connect(nodes, x, y, x + 1, y, r.nextInt(20));
                connect(nodes, x, y, x, y - 1, r.nextInt(20));
                connect(nodes, x, y, x, y + 1, r.nextInt(20));
            }
        }


        for (List<Node> nodeList : nodes) {
            this.nodes.addAll(nodeList);
        }
    }

    private void connect(List<List<Node>> nodes, int x1, int y1, int x2, int y2, int weight) {
        try {
            Node a = nodes.get(y1).get(x1);
            Node b = nodes.get(y2).get(x2);
            a.connect(b, weight);
        } catch (IndexOutOfBoundsException e) {

        }
    }


    @Override
    public boolean hasNext() {
        return true;
    }

    @Override
    public VideoSize getSize() {
        return new VideoSize(1280, 720);
    }

    @Override
    public void provideFrame(Graphics g) {
        super.provideFrame(g);

        int nodeSize = 24;
        int half = nodeSize / 2;
        FontMetrics metrics = g.getFontMetrics();

        for (Node node : nodes) {
            g.setColor(node.color);
            g.fillOval(node.x, node.y, nodeSize, nodeSize);
            for (Map.Entry<Node, Edge> entry : node.edges.entrySet()) {
                g.drawLine(node.x + half,
                        node.y + half,
                        entry.getKey().x + half,
                        entry.getKey().y + half);
            }
            g.setColor(Color.BLACK);
            Rectangle2D rect = metrics.getStringBounds(node.name, g);
            g.drawString(node.name, (int) (node.x + half - rect.getWidth() / 2), node.y + half);
        }
    }


    class Node {
        int x;
        int y;
        String name;
        Color color;
        Map<Node, Edge> edges;

        Node(int x, int y, String name, Color color) {
            this.x = x;
            this.y = y;
            this.name = name;
            this.color = color;
            this.edges = new HashMap<>();
        }

        Node(int x, int y, String name) {
            this(x, y, name, Color.WHITE);
        }

        Edge connect(Node other, int weight) {
            if (!edges.containsKey(other)) {
                Edge edge = new Edge(this, other, weight);
                edges.put(other, edge);
                other.edges.put(this, edge);
                return edge;
            } else return edges.get(other);
        }

        void disconnect(Node other) {
            edges.remove(other);
            other.edges.remove(this);
        }
    }

    class Edge {
        Node a;
        Node b;
        Color color;
        int weight;

        Edge(Node a, Node b, int weight, Color color) {
            this.a = a;
            this.b = b;
            this.color = color;
        }

        Edge(Node a, Node b, int weight) {
            this(a, b, weight, Color.WHITE);
        }
    }
}
