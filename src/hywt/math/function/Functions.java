package hywt.math.function;

import hywt.math.function.basic.ConstantFunction;
import hywt.math.function.basic.PowerFunction;

public class Functions {
    private Functions() {
    }

    public static Function polyFunction(double... series) {
        if (series.length == 0)
            return ConstantFunction.ZERO;
        Function out = new ConstantFunction(series[0]);
        for (int i = 1; i < series.length; i++) {
            if (Double.compare(series[i], 0) != 0) {
                if (Double.compare(series[i], 1) == 0)
                    out = out.add(new PowerFunction(i));
                else
                    out = out.add(new PowerFunction(i).multiply(series[i]));
            }
        }
        return out;
    }

    public static Function simplify(Function f) {
        if (f instanceof ComplexFunction) {
            ComplexFunction f2 = (ComplexFunction) f;
            Function op1 = simplify(f2.op1);
            Function op2 = simplify(f2.op2);
            if (op1 instanceof ConstantFunction && op2 instanceof ConstantFunction) {
                return new ConstantFunction(op1.get(0) + op2.get(0));
            }
            switch (f2.operation) {
                case ADD:
                    if (op1.equals(ConstantFunction.ZERO))
                        return op2;
                    else if (op2.equals(ConstantFunction.ZERO))
                        return op1;

                case SUBTRACT:
                    if (op2.equals(ConstantFunction.ZERO))
                        return op1;

                case MULTIPLY:
                    if (op1.equals(ConstantFunction.ONE))
                        return op2;
                    else if (op2.equals(ConstantFunction.ONE))
                        return op1;
                    else if (op1.equals(ConstantFunction.ZERO) || op2.equals(ConstantFunction.ZERO))
                        return ConstantFunction.ZERO;

                case DIVIDE:
                    if (op2.equals(ConstantFunction.ONE))
                        return op1;
            }
            return new ComplexFunction(op1, op2, f2.operation);
        } else if (f instanceof PowerFunction) {
            PowerFunction f2 = (PowerFunction) f;
            if (Double.compare(f2.exp, 0) == 0) return ConstantFunction.ONE;
        }
        return f;
    }
}