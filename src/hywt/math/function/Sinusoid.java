package hywt.math.function;

import hywt.math.function.basic.PowerFunction;
import hywt.math.function.basic.SineFunction;
import hywt.math.number.Complex;

public class Sinusoid extends Function {
    private final Function f;

    public Sinusoid(double freq, double amp, double phase) {
        f = new CompositeFunction(new SineFunction(), new PowerFunction(1).multiply(2 * Math.PI * freq + phase)).multiply(amp);
    }

    public Sinusoid(double freq, Complex c) {
        f = new Sinusoid(freq, c.length(), 0);
    }

    @Override
    public double get(double x) {
        return f.get(x);
    }

    @Override
    public Function derivative() {
        return f.derivative();
    }

    @Override
    public String toString() {
        return f.toString();
    }
}
