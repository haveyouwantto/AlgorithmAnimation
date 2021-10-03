package hywt.math.shapes;

import hywt.math.function.Mapper;

import java.util.Objects;

public class Color3 {
    public int r;
    public int g;
    public int b;

    public Color3(int r, int g, int b) {
        this.r = r;
        this.g = g;
        this.b = b;
    }

    public Color3(double rp, double gp, double bp) {
        Mapper m = new Mapper(0, 1, 0, 255);
        this.r = (int) m.get(rp);
        this.g = (int) m.get(gp);
        this.b = (int) m.get(bp);
    }

    public Color3(int intVal) {
        this.r = (intVal >> 16) & 0xff;
        this.g = (intVal >> 8) & 0xff;
        this.b = intVal & 0xff;
    }

    public Color3 add(Color3 c) {
        r += c.r;
        g += c.g;
        b += c.b;
        return this;
    }

    public Color3 multiply(double mul) {
        r *= mul;
        g *= mul;
        b *= mul;
        return this;
    }

    public Color3 difference(Color3 c) {
        return new Color3(r - c.r, g - c.g, b - c.b);
    }


    public double distance(Color3 other) {
        return Math.sqrt(
                Math.pow(Math.abs(r - other.r), 2) +
                        Math.pow(Math.abs(g - other.g), 2) +
                        Math.pow(Math.abs(b - other.b), 2)
        );
    }

    public int toInteger() {
        return (Mapper.clamp(r) << 16) | (Mapper.clamp(g) << 8) | Mapper.clamp(b);
    }

    public String toCommandColor() {
        return String.format("%f %f %f", this.r / 255d, this.g / 255d, this.b / 255d);
    }

    public Vector3D toVector3D() {
        return new Vector3D(r, g, b);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Color3 color3 = (Color3) o;
        return r == color3.r &&
                g == color3.g &&
                b == color3.b;
    }

    @Override
    public int hashCode() {
        return Objects.hash(r, g, b);
    }

    @Override
    public String toString() {
        return "Color3{" +
                "r=" + r +
                ", g=" + g +
                ", b=" + b +
                '}';
    }

    @Override
    public Color3 clone() {
        return new Color3(r, g, b);
    }
}
