package hywt.algo.animation.dist;

import java.awt.*;
import java.util.Random;

public class PowerDistribution extends Distribution {

    public PowerDistribution() {
        super();
        Random r = new Random();
        manager.setUpdater(v -> {
            if (r.nextBoolean()) return v;
            else {
                float rand = r.nextFloat();
                if (rand < v / 200d) return v + 1;
                else return v - 1;
            }
        });
    }

    @Override
    public void provideFrame(Graphics g) {
        super.provideFrame(g);
    }
}
