package hywt.math.function.base;

import hywt.math.function.Function;

public class LogFunction extends Function {
    public final double base;
    public final double logBase;

    public LogFunction(double base) {
        this.base = base;
        this.logBase = Math.log(base);
    }

    @Override
    public double get(double x) {
        return Math.log(x) / logBase;
    }

    @Override
    public Function derivative() {
        return new ConstantFunction(1).divide(new PowerFunction(1).multiply(Math.log(base)));
    }

    @Override
    public String toString() {
        return String.format("log(%s,x)", base);
    }
}