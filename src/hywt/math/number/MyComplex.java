package hywt.math.number;

public class MyComplex {
    public double real;
    public double imag;

    public MyComplex(double real, double imag) {
        this.real = real;
        this.imag = imag;
    }

    public MyComplex add(MyComplex other) {
        real += other.real;
        imag += other.imag;
        return this;
    }

    public MyComplex subtract(MyComplex other) {
        real -= other.real;
        imag -= other.imag;
        return this;
    }

    public MyComplex multiply(MyComplex other) {
        double r = real * other.real - imag * other.imag;
        imag = real * other.imag + imag * other.real;
        this.real = r;
        return this;
    }

    public MyComplex divide(MyComplex other) {
        return null;
    }

    public double length() {
        return Math.sqrt(real * real + imag * imag);
    }

    public double getReal() {
        return real;
    }

    public double getImag() {
        return imag;
    }

    public MyComplex clone() {
        return new MyComplex(real, imag);
    }
}