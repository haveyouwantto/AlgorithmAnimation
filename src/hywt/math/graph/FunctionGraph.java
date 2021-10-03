package hywt.math.graph;

import java.awt.Color;
import java.awt.Graphics;

import hywt.math.function.Function;

public class FunctionGraph {
    private int offset;
    private double zoom;
    private Graphics g;

    public FunctionGraph(Graphics g, double zoom) {
        this.offset = 16;
        this.zoom = zoom;
        this.g = g;
        init();
    }

    public FunctionGraph(Graphics g) {
        this.offset = 16;
        this.zoom = 30;
        this.g = g;
        init();
    }

    private void init() {
        g.setColor(new Color(128, 128, 128));
        double step = 1 / zoom;
        for (int i = 0; i < 1000; i += 125) {
            g.drawLine(0, i, 1000, i);
            g.drawLine(i, 0, i, 1000);
            g.drawString(String.format("%.2f", -(i - 500) * step), 502, i - 2);
            g.drawString(String.format("%.2f", (i - 500) * step), i + 2, 498);
        }
        g.setColor(new Color(255, 255, 255));
        g.drawLine(0, 500, 1000, 500);
        g.drawLine(500, 0, 500, 1000);
    }

    public void draw(Function fx, Color c) {
        double last = fx.get(-500 / zoom);
        double step = 1 / zoom;
        g.setColor(c);
        for (double i = -500 * step; i < 500 * step; i += step) {
            double current = fx.get(i);
            if (Double.isInfinite(current) || Double.isNaN(current)) {
                last = current;
                continue;
            }
            g.drawLine((int) ((i - step) * zoom) + 500, (int) -(last * zoom) + 500, (int) (i * zoom) + 500,
                    (int) -(current * zoom) + 500);
            last = current;
        }
        g.drawString(fx.toString(), 8, offset);
        offset += 16;
    }
}