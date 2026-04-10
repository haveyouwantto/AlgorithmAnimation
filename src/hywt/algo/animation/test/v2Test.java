package hywt.algo.animation.test;

import hywt.algo.animation.AnimationV2;
import hywt.algo.datatype.VideoSize;

import java.awt.*;

public class v2Test extends AnimationV2 {
    private int x = 0;
    private int y = 240;

    public v2Test() {
        setStartDelay(30); // 30 frames delay
    }

    @Override
    protected void onAlgorithm() throws InterruptedException {
        setStatus("Waiting for start...");
        waitStart();
        
        setStatus("Algorithm started!");
        for (int i = 0; i < 400; i++) {
            x++;
            setStatus("Moving: x = %d", x);
            waitFrame();
        }
        
        setStatus("Waiting at the end...");
        waitFrames(60);
        
        setStatus("Finished!");
        finish();
    }

    @Override
    protected void onDraw(Graphics g) {
        g.setColor(Color.RED);
        g.fillOval(x, y, 20, 20);
    }

    @Override
    public VideoSize getSize() {
        return new VideoSize(640, 480);
    }
}
