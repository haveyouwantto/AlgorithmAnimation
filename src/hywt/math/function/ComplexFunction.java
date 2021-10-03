package hywt.math.function;

public class ComplexFunction extends Function {
    /**
     *
     */
    private static final long serialVersionUID = 1786581333757907478L;
    public final FunctionOperation operation;
    public final Function op1;
    public final Function op2;

    public ComplexFunction(Function op1, Function op2, FunctionOperation operation) {
        this.operation = operation;
        this.op1 = op1;
        this.op2 = op2;
    }

    @Override
    public double get(double x) {
        switch (operation) {
            case ADD:
                return op1.get(x) + op2.get(x);
            case SUBTRACT:
                return op1.get(x) - op2.get(x);
            case MULTIPLY:
                return op1.get(x) * op2.get(x);
            case DIVIDE:
                return op1.get(x) / op2.get(x);
            default:
                throw new ArithmeticException("Operation not supported.");
        }
    }

    @Override
    public Function derivative() {
        switch (operation) {
            case ADD:
                return op1.derivative().add(op2.derivative());
            case SUBTRACT:
                return op1.derivative().subtract(op2.derivative());
            case MULTIPLY:
                return (op1.derivative().multiply(op2)).add(op1.multiply(op2.derivative()));
            case DIVIDE:
                return ((op1.derivative().multiply(op2)).subtract(op1.multiply(op2.derivative())))
                        .divide(op2.multiply(op2));
            default:
                throw new ArithmeticException("Operation not supported.");
        }
    }

    @Override
    public String toString() {
        switch (operation) {
            case ADD:
                return String.format("(%s+%s)", op1, op2);
            case SUBTRACT:
                return String.format("(%s-%s)", op1, op2);
            case MULTIPLY:
                return String.format("(%s*%s)", op1, op2);
            case DIVIDE:
                return String.format("(%s/%s)", op1, op2);
            default:
                throw new ArithmeticException("Operation not supported.");
        }
    }
}