package hywt.math.function;

import hywt.math.function.Function;

public class CompositeFunction extends Function {

    /**
     *
     */
    private static final long serialVersionUID = 5388911419114055851L;
    public final Function outer;
    public final Function inner;

    public CompositeFunction(Function outer, Function inner) {
        this.outer = outer;
        this.inner = inner;
    }

    @Override
    public double get(double x) {
        return outer.get(inner.get(x));
    }

    @Override
    public Function derivative() {
        return new CompositeFunction(outer.derivative().multiply(inner.derivative()), inner);
    }

    @Override
    public String toString() {
        return outer.toString().replace("x", String.format("(%s)", inner.toString()));
    }
}