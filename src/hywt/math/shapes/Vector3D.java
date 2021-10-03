package hywt.math.shapes;

import java.util.Objects;

public class Vector3D {
    public double x;
    public double y;
    public double z;

    public Vector3D() {
        this(0, 0, 0);
    }

    public Vector3D(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public static Vector3D fromPolar(double r, double hozO, double vertO) {
        double hozR = r * Math.cos(vertO);
        return new Vector3D(
                hozR * Math.cos(hozO),
                hozR * Math.sin(hozO),
                r * Math.sin(vertO)
        );
    }

    public Vector3D add(double x, double y, double z) {
        this.x += x;
        this.y += y;
        this.z += z;
        return this;
    }

    public Vector3D add(Vector3D v) {
        return add(v.x, v.y, v.z);
    }

    public Vector3D subtract(double x, double y, double z) {
        this.x -= x;
        this.y -= y;
        this.z -= z;
        return this;
    }

    public Vector3D subtract(Vector3D v) {
        return subtract(v.x, v.y, v.z);
    }

    public Vector3D multiply(double mul) {
        x *= mul;
        y *= mul;
        z *= mul;
        return this;
    }

    public double length() {
        return Math.sqrt(x * x + y * y + z * z);
    }

    public double product(Vector3D v) {
        return x * v.x + y * v.y + z * v.z;
    }

    public double angle(Vector3D v) {
        return Math.acos(product(v) / length() * v.length());
    }

    @Override
    public String toString() {
        return "Vector3D(" + x + ',' + y + ',' + z + ')';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vector3D vector3D = (Vector3D) o;
        return Double.compare(vector3D.x, x) == 0 &&
                Double.compare(vector3D.y, y) == 0 &&
                Double.compare(vector3D.z, z) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z);
    }

    @Override
    public Vector3D clone() {
        return new Vector3D(x, y, z);
    }
}
