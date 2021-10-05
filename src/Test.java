import hywt.math.function.Function;
import hywt.math.function.Functions;

public class Test {
    public static void main(String[] args) {
        Function function = Functions.polyFunction(0, 0, 2);
        Function derivative = function.derivative();
        Function sim = Functions.simplify(derivative);
        System.out.println(function);
        System.out.println(derivative);
        System.out.println(sim);
    }
}
