package hywt.math.shapes;

public class Line2D {
    public Point2D start;
    public Point2D end;

    public Line2D(Point2D start, Point2D end) {
        this.start = start;
        this.end = end;
    }

    public Line2D(double startX, double startY, double endX, double endY) {
        this.start = new Point2D(startX, startY);
        this.end = new Point2D(endX, endY);
    }

    public Vector2D toVector() {
        return new Vector2D(end.x - start.x, end.y - start.y);
    }

    public double getLength() {
        return this.toVector().length();
    }

    @Override
    public String toString() {
        return "Line2D{" +
            "start=" + start +
            ", end=" + end +
            '}';
    }
}
