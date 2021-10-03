package hywt.math.function;

import hywt.math.function.basic.PowerFunction;

public class Mapper extends Function {
    private final Function mapper;

    public Mapper(double sourceStart, double sourceEnd, double targetStart, double targetEnd) {
        Function xFx = new PowerFunction(1).subtract(sourceStart).divide(sourceEnd - sourceStart);
        Function destFx = Functions.polyFunction(targetStart, targetEnd - targetStart);
        mapper = new CompositeFunction(destFx, xFx);
    }

    public static int clamp(int c) {
        return Math.max(0, Math.min(255, c));
    }

    @Override
    public double get(double x) {
        return mapper.get(x);
    }

    @Override
    public Function derivative() {
        return mapper.derivative();
    }

    @Override
    public String toString() {
        return mapper.toString();
    }
}
