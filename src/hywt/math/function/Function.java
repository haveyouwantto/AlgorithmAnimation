package hywt.math.function;

import java.io.Serializable;

import hywt.math.function.base.ConstantFunction;

public abstract class Function implements Serializable {

    private static final long serialVersionUID = 5757130547219342100L;

    public abstract double get(double x);

    public abstract Function derivative();

    public Function add(Function f) {
        return new ComplexFunction(this, f, FunctionOperation.ADD);
    }

    public Function subtract(Function f) {
        return new ComplexFunction(this, f, FunctionOperation.SUBTRACT);
    }

    public Function multiply(Function f) {
        return new ComplexFunction(this, f, FunctionOperation.MULTIPLY);
    }

    public Function multiply(double mul) {
        return multiply(new ConstantFunction(mul));
    }

    public Function divide(Function f) {
        return new ComplexFunction(this, f, FunctionOperation.DIVIDE);
    }

    public Function divide(double div) {
        return divide(new ConstantFunction(div));
    }
}