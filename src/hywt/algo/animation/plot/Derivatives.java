package hywt.algo.animation.plot;

import hywt.math.function.Function;
import hywt.math.function.Functions;

import java.awt.*;

public class Derivatives extends Plot {
    Function[] functions = new Function[5];
    Color[] palette = new Color[]{Color.RED, Color.ORANGE, Color.YELLOW, Color.GREEN, Color.CYAN, Color.BLUE, Color.MAGENTA};

    public Derivatives() {
        functions[0] = Functions.polyFunction(1, 2, 3, 4, 5, 6);
        for (int i = 1; i < functions.length; i++) {
            functions[i] = Functions.simplify(functions[i - 1].derivative());
        }
    }

    @Override
    public boolean hasNext() {
        return true;
    }

    @Override
    public void provideFrame(Graphics g) {
        clear();

        for (int i = 0; i < functions.length; i++) {
            Function function = functions[i];
            double last = 0;
            for (int j = 0; j < 500; j++) {
                double x = getOriginalX(j);
                double y = function.get(x);
                if (j > 0) addLine(getOriginalX(j - 1), last, x, y, palette[i]);
                last = y;
            }
        }
        super.provideFrame(g);
    }
}
