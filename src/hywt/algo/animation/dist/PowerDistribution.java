package hywt.algo.animation.dist;

import java.awt.*;
import java.util.Random;

public class PowerDistribution extends Distribution {

    public PowerDistribution() {
        super();
        Random r = new Random();
        manager.fill(1);
        manager.setUpdater(() -> {
            for (int i = 0; i < 2000; i++) {
                int index = r.nextInt(manager.size());
                int val = manager.get(index);

                int index2 = r.nextInt(manager.size());
                int val2 = manager.get(index2);
                manager.set(index, val + val2);
                manager.set(index2, 1);
            }
        });
    }

    @Override
    public void provideFrame(Graphics g) {
        super.provideFrame(g);
    }
}
