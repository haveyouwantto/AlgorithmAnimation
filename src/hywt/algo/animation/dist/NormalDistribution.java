package hywt.algo.animation.dist;

import java.awt.*;
import java.util.Random;

public class NormalDistribution extends Distribution {

    public NormalDistribution() {
        super();
        Random r = new Random();
        manager.fill(100);
        manager.setUpdater(() -> {
            for (int i = 0; i < manager.size(); i++) {
                int val = manager.get(i);
                if (r.nextBoolean()) continue;
                else {
                    if (r.nextBoolean()) {
                        manager.set(i, val + 1);
                    } else {
                        manager.set(i, val - 1);
                    }
                }
            }
        });
    }

    @Override
    public void provideFrame(Graphics g) {
        super.provideFrame(g);

        int sum = 0;
        for (int cell : manager) {
            sum += cell;
        }
        double avg = (double) sum / manager.size();

        double devi = 0;
        for (int cell : manager) {
            devi += Math.pow(cell - avg, 2);
        }
        devi /= manager.size();

        g.drawString(String.format("正态分布 - 回合 = %d | 平均数 = %.5f | 方差 = %.5f", generation, avg, devi), 16, 16);
    }
}
