package hywt.math.function.base;

import hywt.math.function.Function;

public class NExpFunction extends Function {

    @Override
    public double get(double x) {
        return Math.pow(Math.E, x);
    }

    @Override
    public Function derivative() {
        return this;
    }

    @Override
    public String toString() {
        return "e^x";
    }
}
