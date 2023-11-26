package hywt.math.shapes;

public class Vector2D implements Cloneable {
    public double x;
    public double y;
    public static final Vector2D ZERO = new Vector2D(0, 0);

    public Vector2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public static Vector2D fromPolar(double radius, double theta) {
        return new Vector2D(
                radius * Math.cos(theta),
                radius * Math.sin(theta)
        );
    }

    public Vector2D add(Vector2D v) {
        this.x += v.x;
        this.y += v.y;
        return this;
    }

    public Vector2D add(double x, double y) {
        this.x += x;
        this.y += y;
        return this;
    }

    public Vector2D subtract(Vector2D v) {
        this.x -= v.x;
        this.y -= v.y;
        return this;
    }

    public Vector2D subtract(double x, double y) {
        this.x -= x;
        this.y -= y;
        return this;
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
    public Vector2D clone() {
        return new Vector2D(x, y);
    }

}