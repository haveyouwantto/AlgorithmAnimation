package hywt.math.function;

import hywt.math.function.base.ConstantFunction;
import hywt.math.function.base.PowerFunction;

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
}