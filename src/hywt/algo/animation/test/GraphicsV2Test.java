package hywt.algo.animation.test;

import hywt.algo.animation.AnimationV2;
import hywt.algo.datatype.VideoSize;
import hywt.algo.gui.GraphicsV2;
import hywt.algo.gui.TextStyle;

import java.awt.*;

public class GraphicsV2Test extends AnimationV2 {
    private String[][] tableData = {
            { "ID", "Value", "Status" },
            { "1", "100", "OK" },
            { "2", "200", "WARN" },
            { "3", "300", "ERROR" }
    };

    @Override
    protected void onAlgorithm() throws InterruptedException {
        waitStart();
        setStatus("Testing GraphicsV2...");
        waitFrames(120);
        finish();
    }

    @Override
    protected void onDraw(GraphicsV2 g) {

        // Test Table
        g.drawTable(tableData, 50, 50, 100, 30,
                TextStyle.create(new Font("Arial", Font.BOLD, 14), Color.YELLOW, "mc"),
                TextStyle.create(null, Color.WHITE, "mc"));

        // Test Alignment
        g.setColor(Color.CYAN);
        g.drawRect(300, 200, 200, 100);
        g.drawString("Center-Middle", 400, 250, TextStyle.align("mc"));
        g.drawString("Top-Left", 300, 200, TextStyle.align("tl"));
        g.drawString("Bottom-Right", 500, 300, TextStyle.align("br"));

        // Test Primitives
        g.drawBar(50, 250, 40, 150, Color.GREEN, Color.WHITE);
        g.drawArrow(new hywt.math.shapes.Point2D(150, 350), new hywt.math.shapes.Point2D(250, 250), 10, Color.RED);
    }

    @Override
    public VideoSize getSize() {
        return new VideoSize(640, 480);
    }
}
