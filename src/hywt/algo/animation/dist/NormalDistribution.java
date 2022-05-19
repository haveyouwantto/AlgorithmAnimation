package hywt.algo.animation.dist;

import java.awt.*;
import java.util.Random;

public class NormalDistribution extends Distribution {

    public NormalDistribution() {
        super();
        Random r = new Random();
        manager.setUpdater(v -> {
            if (r.nextBoolean()) return v;
            else if (r.nextBoolean()) return v + 1;
            else return v - 1;
        });
    }

    @Override
    public void provideFrame(Graphics g) {
        super.provideFrame(g);

        int[] cells = manager.getCells();
        int sum = 0;
        for (int cell : cells) {
            sum += cell;
        }
        double avg = (double) sum / cells.length;

        double devi = 0;
        for (int cell : cells) {
            devi += Math.pow(cell - avg, 2);
        }
        devi /= cells.length;

        g.drawString(String.format("正态分布 - 回合 = %d | 平均数 = %.5f | 方差 = %.5f", generation, avg, devi), 16, 16);
    }
}
