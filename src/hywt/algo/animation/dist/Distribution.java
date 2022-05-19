package hywt.algo.animation.dist;

import hywt.algo.animation.BasicAnimation;
import hywt.algo.datatype.CellManager;
import hywt.algo.datatype.VideoSize;

import java.awt.*;

public class Distribution extends BasicAnimation {
    protected CellManager manager;
    double[] freq;
    int frame;
    int generation;

    public Distribution() {
        manager = new CellManager(120, 120);
        freq = new double[202];
        generation = 0;
    }

    @Override
    public boolean hasNext() {
        return frame < 30 * 120;
    }

    @Override
    public VideoSize getSize() {
        return new VideoSize(850, 480);
    }

    @Override
    public void provideFrame(Graphics g) {
        super.provideFrame(g);
        for (int y = 0; y < 120; y++) {
            for (int x = 0; x < 120; x++) {
                g.setColor(Color.getHSBColor(manager.get(x, y) / 200f * (2 / 3f), 1, 1));
                g.fillRect(60 + x * 2, 120 + y * 2, 2, 2);
            }
        }

        manager.getFrequency(freq);
        //System.out.println(Arrays.toString(freq));

        for (int y = 0; y <= 200; y++) {
            g.setColor(Color.getHSBColor((float) (y / 200d * (2 / 3d)), 1, 1));
            g.fillRect(650, 60 + y * 2, 8, 2);
            g.setColor(Color.WHITE);
            //System.out.println(freq[y]);
            g.fillRect(659, 60 + y * 2, (int) (freq[y] * 3000), 2);
            if (y % 20 == 0) {
                g.drawLine(640, 60 + y * 2, 649, 60 + y * 2);
                g.drawString(String.valueOf(y), 610, 67 + y * 2);
            }
        }
        if (frame > 150 && frame % 2 == 0) {
            manager.update();
            generation++;
        }
        frame++;
    }
}
