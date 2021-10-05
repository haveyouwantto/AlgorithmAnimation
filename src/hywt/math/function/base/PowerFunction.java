package hywt.math.function.base;

import hywt.math.function.Function;

public class PowerFunction extends Function {

    /**
     *
     */
    private static final long serialVersionUID = -8060482556202483538L;
    public final double exp;

    public PowerFunction(double exp) {
        this.exp = exp;
    }

    @Override
    public double get(double x) {
        return Math.pow(x, exp);
    }

    @Override
    public Function derivative() {
        return new ConstantFunction(exp).multiply(new PowerFunction(exp - 1));
    }

    @Override
    public String toString() {
        if (Double.compare(exp, 1) == 0)
            return "x";
        else if (Double.compare(exp, 0) == 0)
            return "1";
        return String.format("x^%s", exp);
    }

}