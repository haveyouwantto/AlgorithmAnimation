package hywt.algo.animation.graph.v2;

import java.awt.*;
import java.util.*;

public class PrimMST extends WeightedGraph {
    private LinkedList<Node> connectedNodes;
    private Set<Edge> found;

    private Object lock = new Object();

    public PrimMST() {
        found = new HashSet<>();
        connectedNodes = new LinkedList<>();

        connectedNodes.addLast(nodes.remove(0));
    }

    private void step(){
        Node node = connectedNodes.removeFirst();
        for(Edge edge:node.edges.values()){
            if(!connectedNodes.contains(edge.a)) connectedNodes.add(edge.a);
            if(!connectedNodes.contains(edge.b)) connectedNodes.add(edge.b);
        }
    }

    @Override
    public void provideFrame(Graphics g) {
        lock.notify();
        super.provideFrame(g);
    }
}
