package hywt.math.function.basic;

import hywt.math.function.Function;

public class NLogFunction extends Function{

    @Override
    public double get(double x) {
        return Math.log(x);
    }

    @Override
    public Function derivative() {
        return new ConstantFunction(1).divide(new PowerFunction(1));
    }

    @Override
    public String toString() {
        return "ln(x)";
    }
}
