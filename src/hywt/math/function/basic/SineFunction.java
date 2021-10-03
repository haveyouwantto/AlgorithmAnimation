package hywt.math.function.basic;

import hywt.math.function.Function;

public class SineFunction extends Function {

    /**
     *
     */
    private static final long serialVersionUID = 8001172276105542525L;

    @Override
    public double get(double x) {
        return Math.sin(x);
    }

    @Override
    public Function derivative() {
        return new CosineFunction();
    }

    @Override
    public String toString() {
        return "sin(x)";
    }
    
}