import hywt.math.function.Mapper;

public class Test {
    public static void main(String[] args) {
        Mapper mapper = new Mapper(-2, 2, 0, 14);

        System.out.println(mapper.get(1));
    }
}
