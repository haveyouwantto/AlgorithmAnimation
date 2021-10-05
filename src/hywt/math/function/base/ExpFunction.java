package hywt.math.function.base;

import hywt.math.function.Function;

public class ExpFunction extends Function{

    /**
     *
     */
    private static final long serialVersionUID = 5641790253911135639L;
    public final double base;

    public ExpFunction(double base) {
        this.base = base;
    }

    @Override
    public double get(double x) {
        return Math.pow(base, x);
    }

    @Override
    public Function derivative() {
        return this.multiply(Math.log(base));
    }

    @Override
    public String toString() {
        return String.format("%s^x", base);
    }
}