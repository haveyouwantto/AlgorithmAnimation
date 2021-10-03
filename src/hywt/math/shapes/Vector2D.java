package hywt.math.shapes;

public class Vector2D implements Cloneable {
    public double x;
    public double y;
    public static final Vector2D ZERO = new Vector2D(0, 0);

    public Vector2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public static Vector2D fromPolar(double r, double o) {
        return new Vector2D(
                r * Math.cos(o),
                r * Math.sin(o)
        );
    }

    public void add(Vector2D v) {
        this.x += v.x;
        this.y += v.y;
    }

    public void add(double x, double y) {
        this.x += x;
        this.y += y;
    }

    public void subtract(Vector2D v) {
        this.x -= v.x;
        this.y -= v.y;
    }

    public void subtract(double x, double y) {
        this.x -= x;
        this.y -= y;
    }

    public Vector2D multiply(double n) {
        return new Vector2D(x * n, y * n);
    }

    public Vector2D divide(double n) {
        return new Vector2D(x / n, y / n);
    }

    @Override
    public String toString() {
        return "Vector2D{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }

    public double length() {
        return Math.sqrt(x * x + y * y);
    }

    public double getAngle() {
        if (x == 0) {
            if (y == 0) return Double.NaN;
            return y > 0 ? Math.toRadians(90) : Math.toRadians(270);
        }
        return Math.atan(y / x);
    }

    public Vector2D reverse() {
        return new Vector2D(-this.x, -this.y);
    }

    @Override
    public Object clone() {
        return new Vector2D(x, y);
    }

}