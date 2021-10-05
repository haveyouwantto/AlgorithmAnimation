package hywt.math.function.base;

import hywt.math.function.Function;

public class CosineFunction extends Function {

    /**
     *
     */
    private static final long serialVersionUID = 2326434620702844560L;

    @Override
    public double get(double x) {
        return Math.cos(x);
    }

    @Override
    public Function derivative() {
        return new ConstantFunction(-1).multiply(new SineFunction());
    }
    
    @Override
    public String toString() {
        return "cos(x)";
    }
}