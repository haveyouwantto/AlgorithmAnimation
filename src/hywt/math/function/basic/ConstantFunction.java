package hywt.math.function.basic;

import hywt.math.function.Function;

public class ConstantFunction extends Function implements Comparable<ConstantFunction> {

    /**
     *
     */
    private static final long serialVersionUID = 2295492720087613300L;
    public static final Function ZERO = new ConstantFunction(0);
    public static final Function ONE = new ConstantFunction(1);

    public final double cons;

    public ConstantFunction(double cons) {
        this.cons = cons;
    }

    @Override
    public double get(double x) {
        return cons;
    }

    @Override
    public Function derivative() {
        return new ConstantFunction(0);
    }

    @Override
    public String toString() {
        return String.valueOf(cons);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        long temp;
        temp = Double.doubleToLongBits(cons);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ConstantFunction other = (ConstantFunction) obj;
        if (Double.doubleToLongBits(cons) != Double.doubleToLongBits(other.cons))
            return false;
        return true;
    }

    @Override
    public int compareTo(ConstantFunction arg0) {
        return Double.compare(cons, arg0.cons);
    }

}