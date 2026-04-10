package hywt.algo.gui;

import hywt.math.shapes.Point2D;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ImageObserver;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.RenderableImage;
import java.text.AttributedCharacterIterator;
import java.util.Map;
import java.util.Stack;

/**
 * GraphicsV2 is a high-level wrapper and a fully functional Graphics2D
 * delegate.
 */
public class GraphicsV2 extends Graphics2D {
    private final Graphics2D g2d;
    private final Stack<State> stateStack = new Stack<>();

    public GraphicsV2(Graphics g) {
        if (g instanceof Graphics2D) {
            this.g2d = (Graphics2D) g;
        } else {
            throw new IllegalArgumentException("Graphics object must be an instance of Graphics2D");
        }
        // Enable antialiasing by default
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
    }

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
        if (text == null || text.isEmpty())
            return;

        pushState();
        if (style != null) {
            if (style.getFont() != null)
                g2d.setFont(style.getFont());
            if (style.getColor() != null)
                g2d.setColor(style.getColor());
        }

        FontMetrics fm = g2d.getFontMetrics();
        double offsetX = 0;
        double offsetY = 0;

        String align = (style != null && style.getAlignment() != null) ? style.getAlignment().toLowerCase() : "ml";

        // Horizontal Alignment
        if (align.contains("c")) {
            offsetX = -fm.stringWidth(text) / 2.0;
        } else if (align.contains("r")) {
            offsetX = -fm.stringWidth(text);
        }

        // Vertical Alignment
        if (align.contains("t")) {
            offsetY = fm.getAscent();
        } else if (align.contains("m")) {
            offsetY = fm.getAscent() - fm.getHeight() / 2.0;
        } else if (align.contains("b")) {
            offsetY = 0;
        }

        g2d.drawString(text, (float) (x + offsetX), (float) (y + offsetY));
        popState();
    }

    public void drawString(String text, int x, int y, TextStyle style) {
        drawString(text, (double) x, (double) y, style);
    }

    public void drawString(String text, float x, float y, TextStyle style) {
        drawString(text, (double) x, (double) y, style);
    }

    public void drawBar(double x, double y, double w, double h, Color fill, Color border) {
        if (fill != null) {
            Color old = g2d.getColor();
            g2d.setColor(fill);
            g2d.fillRect((int) x, (int) y, (int) w, (int) h);
            g2d.setColor(old);
        }
        if (border != null) {
            Color old = g2d.getColor();
            g2d.setColor(border);
            g2d.drawRect((int) x, (int) y, (int) w, (int) h);
            g2d.setColor(old);
        }
    }

    public void drawPoint(Point2D p, double radius, Color color) {
        pushState();
        if (color != null)
            g2d.setColor(color);
        g2d.fillOval((int) (p.x - radius), (int) (p.y - radius), (int) (radius * 2), (int) (radius * 2));
        popState();
    }

    public void drawLine(Point2D p1, Point2D p2, Color color, float thickness) {
        pushState();
        if (color != null)
            g2d.setColor(color);
        g2d.setStroke(new BasicStroke(thickness));
        g2d.drawLine((int) p1.x, (int) p1.y, (int) p2.x, (int) p2.y);
        popState();
    }

    public void drawArrow(Point2D start, Point2D end, double headSize, Color color) {
        pushState();
        if (color != null)
            g2d.setColor(color);
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

    public void drawTable(String[][] data, double x, double y, double cellW, double cellH, TextStyle headerStyle,
            TextStyle cellStyle) {
        if (data == null || data.length == 0)
            return;
        for (int row = 0; row < data.length; row++) {
            for (int col = 0; col < data[row].length; col++) {
                drawTableCell(data[row][col], x, y, col, row, cellW, cellH, headerStyle, cellStyle);
            }
        }
    }

    public void drawTable(int[][] data, double x, double y, double cellW, double cellH, TextStyle headerStyle,
            TextStyle cellStyle) {
        if (data == null || data.length == 0)
            return;
        for (int row = 0; row < data.length; row++) {
            for (int col = 0; col < data[row].length; col++) {
                drawTableCell(String.valueOf(data[row][col]), x, y, col, row, cellW, cellH, headerStyle, cellStyle);
            }
        }
    }

    public void drawTable(double[][] data, double x, double y, double cellW, double cellH, TextStyle headerStyle,
            TextStyle cellStyle) {
        if (data == null || data.length == 0)
            return;
        for (int row = 0; row < data.length; row++) {
            for (int col = 0; col < data[row].length; col++) {
                drawTableCell(String.valueOf(data[row][col]), x, y, col, row, cellW, cellH, headerStyle, cellStyle);
            }
        }
    }

    public void drawTable(Object[][] data, double x, double y, double cellW, double cellH, TextStyle headerStyle,
            TextStyle cellStyle) {
        if (data == null || data.length == 0)
            return;
        for (int row = 0; row < data.length; row++) {
            for (int col = 0; col < data[row].length; col++) {
                drawTableCell(String.valueOf(data[row][col]), x, y, col, row, cellW, cellH, headerStyle, cellStyle);
            }
        }
    }

    private void drawTableCell(String value, double x, double y, int col, int row, double cellW, double cellH,
            TextStyle headerStyle, TextStyle cellStyle) {
        double cx = x + col * cellW;
        double cy = y + row * cellH;
        g2d.setColor(Color.DARK_GRAY);
        g2d.drawRect((int) cx, (int) cy, (int) cellW, (int) cellH);
        TextStyle style = (row == 0) ? headerStyle : cellStyle;
        if (style == null)
            style = TextStyle.align("mc");
        drawString(value, cx + cellW / 2.0, cy + cellH / 2.0, style);
    }

    // --- Graphics2D Delegation ---

    @Override
    public void draw(Shape s) {
        g2d.draw(s);
    }

    @Override
    public boolean drawImage(Image img, AffineTransform xform, ImageObserver obs) {
        return g2d.drawImage(img, xform, obs);
    }

    @Override
    public void drawImage(BufferedImage img, BufferedImageOp op, int x, int y) {
        g2d.drawImage(img, op, x, y);
    }

    @Override
    public void drawRenderedImage(RenderedImage img, AffineTransform xform) {
        g2d.drawRenderedImage(img, xform);
    }

    @Override
    public void drawRenderableImage(RenderableImage img, AffineTransform xform) {
        g2d.drawRenderableImage(img, xform);
    }

    @Override
    public void drawString(String str, int x, int y) {
        drawString(str, (double) x, (double) y, null);
    }

    @Override
    public void drawString(String str, float x, float y) {
        drawString(str, (double) x, (double) y, null);
    }

    @Override
    public void drawString(AttributedCharacterIterator iterator, int x, int y) {
        g2d.drawString(iterator, x, y);
    }

    @Override
    public void drawString(AttributedCharacterIterator iterator, float x, float y) {
        g2d.drawString(iterator, x, y);
    }

    @Override
    public void drawGlyphVector(GlyphVector g, float x, float y) {
        g2d.drawGlyphVector(g, x, y);
    }

    @Override
    public void fill(Shape s) {
        g2d.fill(s);
    }

    @Override
    public boolean hit(Rectangle rect, Shape s, boolean onStroke) {
        return g2d.hit(rect, s, onStroke);
    }

    @Override
    public GraphicsConfiguration getDeviceConfiguration() {
        return g2d.getDeviceConfiguration();
    }

    @Override
    public void setComposite(Composite comp) {
        g2d.setComposite(comp);
    }

    @Override
    public void setPaint(Paint paint) {
        g2d.setPaint(paint);
    }

    @Override
    public void setStroke(Stroke s) {
        g2d.setStroke(s);
    }

    @Override
    public void setRenderingHint(RenderingHints.Key hintKey, Object hintValue) {
        g2d.setRenderingHint(hintKey, hintValue);
    }

    @Override
    public Object getRenderingHint(RenderingHints.Key hintKey) {
        return g2d.getRenderingHint(hintKey);
    }

    @Override
    public void setRenderingHints(Map<?, ?> hints) {
        g2d.setRenderingHints(hints);
    }

    @Override
    public void addRenderingHints(Map<?, ?> hints) {
        g2d.addRenderingHints(hints);
    }

    @Override
    public RenderingHints getRenderingHints() {
        return g2d.getRenderingHints();
    }

    @Override
    public void translate(int x, int y) {
        g2d.translate(x, y);
    }

    @Override
    public void translate(double tx, double ty) {
        g2d.translate(tx, ty);
    }

    @Override
    public void rotate(double theta) {
        g2d.rotate(theta);
    }

    @Override
    public void rotate(double theta, double x, double y) {
        g2d.rotate(theta, x, y);
    }

    @Override
    public void scale(double sx, double sy) {
        g2d.scale(sx, sy);
    }

    @Override
    public void shear(double shx, double shy) {
        g2d.shear(shx, shy);
    }

    @Override
    public void transform(AffineTransform xform) {
        g2d.transform(xform);
    }

    @Override
    public void setTransform(AffineTransform xform) {
        g2d.setTransform(xform);
    }

    @Override
    public AffineTransform getTransform() {
        return g2d.getTransform();
    }

    @Override
    public Paint getPaint() {
        return g2d.getPaint();
    }

    @Override
    public Composite getComposite() {
        return g2d.getComposite();
    }

    @Override
    public void setBackground(Color color) {
        g2d.setBackground(color);
    }

    @Override
    public Color getBackground() {
        return g2d.getBackground();
    }

    @Override
    public Stroke getStroke() {
        return g2d.getStroke();
    }

    @Override
    public void clip(Shape s) {
        g2d.clip(s);
    }

    @Override
    public FontRenderContext getFontRenderContext() {
        return g2d.getFontRenderContext();
    }

    // --- Graphics (Legacy) Delegation ---

    @Override
    public Graphics create() {
        return new GraphicsV2(g2d.create());
    }

    @Override
    public Color getColor() {
        return g2d.getColor();
    }

    @Override
    public void setColor(Color c) {
        g2d.setColor(c);
    }

    @Override
    public void setPaintMode() {
        g2d.setPaintMode();
    }

    @Override
    public void setXORMode(Color c1) {
        g2d.setXORMode(c1);
    }

    @Override
    public Font getFont() {
        return g2d.getFont();
    }

    @Override
    public void setFont(Font font) {
        g2d.setFont(font);
    }

    @Override
    public FontMetrics getFontMetrics(Font f) {
        return g2d.getFontMetrics(f);
    }

    @Override
    public Rectangle getClipBounds() {
        return g2d.getClipBounds();
    }

    @Override
    public void clipRect(int x, int y, int width, int height) {
        g2d.clipRect(x, y, width, height);
    }

    @Override
    public void setClip(int x, int y, int width, int height) {
        g2d.setClip(x, y, width, height);
    }

    @Override
    public Shape getClip() {
        return g2d.getClip();
    }

    @Override
    public void setClip(Shape clip) {
        g2d.setClip(clip);
    }

    @Override
    public void copyArea(int x, int y, int width, int height, int dx, int dy) {
        g2d.copyArea(x, y, width, height, dx, dy);
    }

    @Override
    public void drawLine(int x1, int y1, int x2, int y2) {
        g2d.drawLine(x1, y1, x2, y2);
    }

    @Override
    public void fillRect(int x, int y, int width, int height) {
        g2d.fillRect(x, y, width, height);
    }

    @Override
    public void clearRect(int x, int y, int width, int height) {
        g2d.clearRect(x, y, width, height);
    }

    @Override
    public void drawRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight) {
        g2d.drawRoundRect(x, y, width, height, arcWidth, arcHeight);
    }

    @Override
    public void fillRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight) {
        g2d.fillRoundRect(x, y, width, height, arcWidth, arcHeight);
    }

    @Override
    public void drawOval(int x, int y, int width, int height) {
        g2d.drawOval(x, y, width, height);
    }

    @Override
    public void fillOval(int x, int y, int width, int height) {
        g2d.fillOval(x, y, width, height);
    }

    @Override
    public void drawArc(int x, int y, int width, int height, int startAngle, int arcAngle) {
        g2d.drawArc(x, y, width, height, startAngle, arcAngle);
    }

    @Override
    public void fillArc(int x, int y, int width, int height, int startAngle, int arcAngle) {
        g2d.fillArc(x, y, width, height, startAngle, arcAngle);
    }

    @Override
    public void drawPolyline(int[] xPoints, int[] yPoints, int nPoints) {
        g2d.drawPolyline(xPoints, yPoints, nPoints);
    }

    @Override
    public void drawPolygon(int[] xPoints, int[] yPoints, int nPoints) {
        g2d.drawPolygon(xPoints, yPoints, nPoints);
    }

    @Override
    public void fillPolygon(int[] xPoints, int[] yPoints, int nPoints) {
        g2d.fillPolygon(xPoints, yPoints, nPoints);
    }

    @Override
    public boolean drawImage(Image img, int x, int y, ImageObserver observer) {
        return g2d.drawImage(img, x, y, observer);
    }

    @Override
    public boolean drawImage(Image img, int x, int y, int width, int height, ImageObserver observer) {
        return g2d.drawImage(img, x, y, width, height, observer);
    }

    @Override
    public boolean drawImage(Image img, int x, int y, Color bgcolor, ImageObserver observer) {
        return g2d.drawImage(img, x, y, bgcolor, observer);
    }

    @Override
    public boolean drawImage(Image img, int x, int y, int width, int height, Color bgcolor, ImageObserver observer) {
        return g2d.drawImage(img, x, y, width, height, bgcolor, observer);
    }

    @Override
    public boolean drawImage(Image img, int dx1, int dy1, int dx2, int dy2, int sx1, int sy1, int sx2, int sy2,
            ImageObserver observer) {
        return g2d.drawImage(img, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, observer);
    }

    @Override
    public boolean drawImage(Image img, int dx1, int dy1, int dx2, int dy2, int sx1, int sy1, int sx2, int sy2,
            Color bgcolor, ImageObserver observer) {
        return g2d.drawImage(img, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, bgcolor, observer);
    }

    @Override
    public void dispose() {
        g2d.dispose();
    }
}
