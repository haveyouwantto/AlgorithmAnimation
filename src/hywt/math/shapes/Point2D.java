package hywt.math.shapes;

public class Point2D {
    public double x;
    public double y;

    public Point2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "Point2D{" +
            "x=" + x +
            ", y=" + y +
            '}';
    }
}
