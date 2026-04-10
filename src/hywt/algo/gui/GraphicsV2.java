package hywt.algo.gui;

import hywt.math.shapes.Point2D;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.Stack;

/**
 * GraphicsV2 is a high-level wrapper for Graphics2D.
 * Optimized for "Demoscene" style (crisp, aliased rendering).
 */
public class GraphicsV2 {
    private final Graphics2D g2d;
    private final Stack<State> stateStack = new Stack<>();

    public GraphicsV2(Graphics g) {
        if (g instanceof Graphics2D) {
            this.g2d = (Graphics2D) g;
        } else {
            throw new IllegalArgumentException("Graphics object must be an instance of Graphics2D");
        }

        // --- Demoscene / Pixel-perfect Rendering Hints ---
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_OFF);
    }

    public Graphics2D getG2D() {
        return g2d;
    }

    // --- Essential Delegation ---

    public void setColor(Color c) { g2d.setColor(c); }
    public Color getColor() { return g2d.getColor(); }
    public void setFont(Font f) { g2d.setFont(f); }
    public Font getFont() { return g2d.getFont(); }
    public void setStroke(Stroke s) { g2d.setStroke(s); }
    public Stroke getStroke() { return g2d.getStroke(); }
    
    public void fillRect(int x, int y, int width, int height) { g2d.fillRect(x, y, width, height); }
    public void drawRect(int x, int y, int width, int height) { g2d.drawRect(x, y, width, height); }
    public void drawLine(int x1, int y1, int x2, int y2) { g2d.drawLine(x1, y1, x2, y2); }

    // --- High-level Utility Methods ---

    public void pushState() {
        stateStack.push(new State(g2d.getColor(), g2d.getFont(), g2d.getStroke(), g2d.getTransform(),
                g2d.getComposite(), g2d.getPaint(), g2d.getBackground()));
    }

    public void popState() {
        if (!stateStack.isEmpty()) {
            State state = stateStack.pop();
            g2d.setColor(state.color);
            g2d.setFont(state.font);
            g2d.setStroke(state.stroke);
            g2d.setTransform(state.transform);
            g2d.setComposite(state.composite);
            g2d.setPaint(state.paint);
            g2d.setBackground(state.background);
        }
    }

    private static class State {
        final Color color;
        final Font font;
        final Stroke stroke;
        final AffineTransform transform;
        final Composite composite;
        final Paint paint;
        final Color background;

        State(Color color, Font font, Stroke stroke, AffineTransform transform, Composite composite, Paint paint,
                Color background) {
            this.color = color;
            this.font = font;
            this.stroke = stroke;
            this.transform = transform;
            this.composite = composite;
            this.paint = paint;
            this.background = background;
        }
    }

    public void drawString(String text, double x, double y, TextStyle style) {
        if (text == null || text.isEmpty()) return;

        pushState();
        if (style != null) {
            if (style.getFont() != null) g2d.setFont(style.getFont());
            if (style.getColor() != null) g2d.setColor(style.getColor());
        }

        FontMetrics fm = g2d.getFontMetrics();
        double offsetX = 0;
        double offsetY = 0;

        String align = (style != null && style.getAlignment() != null) ? style.getAlignment().toLowerCase() : "ml";

        if (align.contains("c")) offsetX = -fm.stringWidth(text) / 2.0;
        else if (align.contains("r")) offsetX = -fm.stringWidth(text);

        if (align.contains("t")) offsetY = fm.getAscent();
        else if (align.contains("m")) offsetY = fm.getAscent() - fm.getHeight() / 2.0;

        g2d.drawString(text, (int) (x + offsetX), (int) (y + offsetY));
        popState();
    }

    public void drawString(String text, double x, double y) {
        drawString(text, x, y, null);
    }

    public void drawBar(double x, double y, double w, double h, Color fill, Color border) {
        if (fill != null) {
            g2d.setColor(fill);
            g2d.fillRect((int) x, (int) y, (int) w, (int) h);
        }
        if (border != null) {
            g2d.setColor(border);
            g2d.drawRect((int) x, (int) y, (int) w, (int) h);
        }
    }

    public void drawPoint(Point2D p, double radius, Color color) {
        pushState();
        if (color != null) g2d.setColor(color);
        g2d.fillOval((int) (p.x - radius), (int) (p.y - radius), (int) (radius * 2), (int) (radius * 2));
        popState();
    }

    public void drawArrow(Point2D start, Point2D end, double headSize, Color color) {
        pushState();
        if (color != null) g2d.setColor(color);
        g2d.drawLine((int) start.x, (int) start.y, (int) end.x, (int) end.y);
        double angle = Math.atan2(end.y - start.y, end.x - start.x);
        int x1 = (int) (end.x - headSize * Math.cos(angle - Math.PI / 6));
        int y1 = (int) (end.y - headSize * Math.sin(angle - Math.PI / 6));
        int x2 = (int) (end.x - headSize * Math.cos(angle + Math.PI / 6));
        int y2 = (int) (end.y - headSize * Math.sin(angle + Math.PI / 6));
        g2d.drawLine((int) end.x, (int) end.y, x1, y1);
        g2d.drawLine((int) end.x, (int) end.y, x2, y2);
        popState();
    }

    public void drawTable(int[][] data, double x, double y, double cellW, double cellH, TextStyle headerStyle, TextStyle cellStyle) {
        if (data == null || data.length == 0) return;
        for (int row = 0; row < data.length; row++) {
            for (int col = 0; col < data[row].length; col++) {
                drawTableCell(String.valueOf(data[row][col]), x, y, col, row, cellW, cellH, headerStyle, cellStyle);
            }
        }
    }

    public void drawTable(String[][] data, double x, double y, double cellW, double cellH, TextStyle headerStyle, TextStyle cellStyle) {
        if (data == null || data.length == 0) return;
        for (int row = 0; row < data.length; row++) {
            for (int col = 0; col < data[row].length; col++) {
                drawTableCell(data[row][col], x, y, col, row, cellW, cellH, headerStyle, cellStyle);
            }
        }
    }

    private void drawTableCell(String value, double x, double y, int col, int row, double cellW, double cellH, TextStyle headerStyle, TextStyle cellStyle) {
        double cx = x + col * cellW;
        double cy = y + row * cellH;
        g2d.setColor(Color.DARK_GRAY);
        g2d.drawRect((int) cx, (int) cy, (int) cellW, (int) cellH);
        TextStyle style = (row == 0) ? headerStyle : cellStyle;
        if (style == null) style = TextStyle.align("mc");
        drawString(value, cx + cellW / 2.0, cy + cellH / 2.0, style);
    }
}
