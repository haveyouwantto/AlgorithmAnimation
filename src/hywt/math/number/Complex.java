package hywt.math.number;

public class Complex {
    public double real;
    public double imag;

    public Complex(double real, double imag) {
        this.real = real;
        this.imag = imag;
    }

    public Complex add(Complex other) {
        real += other.real;
        imag += other.imag;
        return this;
    }

    public Complex subtract(Complex other) {
        real -= other.real;
        imag -= other.imag;
        return this;
    }

    public Complex multiply(Complex other) {
        double r = real * other.real - imag * other.imag;
        imag = real * other.imag + imag * other.real;
        this.real = r;
        return this;
    }

    public Complex divide(Complex other) {
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

    public Complex clone() {
        return new Complex(real, imag);
    }
}