package hywt.math.function;

import hywt.math.function.basic.ConstantFunction;

import java.io.Serializable;

public abstract class Function implements Serializable {

    private static final long serialVersionUID = 5757130547219342100L;

    public abstract double get(double x);

    public abstract Function derivative();

    public Function add(Function f) {
        return new ComplexFunction(this, f, FunctionOperation.ADD);
    }

    public Function add(double n) {
        return add(new ConstantFunction(n));
    }

    public Function subtract(Function f) {
        return new ComplexFunction(this, f, FunctionOperation.SUBTRACT);
    }

    public Function subtract(double n) {
        return subtract(new ConstantFunction(n));
    }

    public Function multiply(Function f) {
        return new ComplexFunction(this, f, FunctionOperation.MULTIPLY);
    }

    public Function multiply(double n) {
        return multiply(new ConstantFunction(n));
    }

    public Function divide(Function f) {
        return new ComplexFunction(this, f, FunctionOperation.DIVIDE);
    }

    public Function divide(double n) {
        return divide(new ConstantFunction(n));
    }
}