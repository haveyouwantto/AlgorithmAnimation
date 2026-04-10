package hywt.algo.animation.dp;

import java.awt.Color;

import hywt.algo.animation.AnimationV2;
import hywt.algo.datatype.VideoSize;
import hywt.algo.gui.GraphicsV2;
import hywt.algo.gui.TextStyle;

public class Knapsack extends AnimationV2 {

    int[][] dp;
    int[] w;
    int[] v;
    int capacity;

    TextStyle gridStyle = TextStyle.create(null, Color.WHITE, "mc");
    TextStyle labelStyle = TextStyle.create(null, Color.YELLOW, "mc");

    int currentGridX = -1;
    int currentGridY = -1;

    int lookbehindGridX = -1;
    int lookbehindGridY = -1;

    Color currColor = new Color(64, 0, 0);
    Color prevColor = new Color(0, 0, 64);
    Color takeColor = new Color(64, 64, 0);
    Color backRegion = new Color(32, 32, 32);

    public Knapsack() {
        // Generate random w, v, capacity
        int n = 10;
        int capacity = 20;
        w = new int[n];
        v = new int[n];
        for (int i = 0; i < n; i++) {
            w[i] = (int) (Math.random() * 10) + 1;
            v[i] = (int) (Math.random() * 10) + 1;
        }
        this.capacity = capacity;
        this.dp = new int[n][capacity];
        setSize(854, 480);
    }

    public Knapsack(int[] w, int[] v, int capacity) {
        this.w = w;
        this.v = v;
        this.capacity = capacity;
        this.dp = new int[w.length][capacity];
        setSize(640, 480);
    }

    @Override
    protected void onAlgorithm() throws InterruptedException {
        waitFrames(60);

        // 0-1 背包问题
        for (int i = 1; i < w.length; i++) {
            for (int j = 1; j < capacity; j++) {
                if (j < w[i]) {
                    currentGridX = j;
                    currentGridY = i;
                    waitFrame();
                    dp[i][j] = dp[i - 1][j];
                    waitFrame();
                } else {
                    currentGridX = j;
                    currentGridY = i;
                    waitFrame();
                    lookbehindGridX = j - w[i];
                    lookbehindGridY = i - 1;
                    waitFrame();
                    dp[i][j] = Math.max(dp[i - 1][j], dp[i - 1][j - w[i]] + v[i]);
                    waitFrame();
                }
            }
        }
        return;
    }

    @Override
    protected void onDraw(GraphicsV2 g) {
        int cellWidth = 32;
        int cellHeight = 16;

        // Draw the highlighted grid cell
        if (currentGridX != -1 && currentGridY != -1) {
            g.setColor(currColor);
            g.fillRect(100 + cellWidth * currentGridX, 100 + cellHeight * currentGridY, cellWidth,
                    cellHeight);
            g.setColor(prevColor);
            g.fillRect(100 + cellWidth * lookbehindGridX, 100 + cellHeight * (lookbehindGridY - 1), cellWidth,
                    cellHeight);
        }

        // Draw the previous grid cell and region
        if (lookbehindGridX != -1 && lookbehindGridY != -1) {
            g.setColor(backRegion);
            g.fillRect(100, 100, cellWidth * lookbehindGridX, cellHeight * lookbehindGridY);
            g.setColor(takeColor);
            g.fillRect(100 + cellWidth * lookbehindGridX, 100 + cellHeight * (lookbehindGridY - 1), cellWidth,
                    cellHeight);
        }

        // Draw the grid
        g.drawTable(dp, 100, 100, cellWidth, cellHeight, gridStyle, gridStyle);

        // Draw capacity and value
        for (int i = 0; i < capacity; i++) {
            g.drawString(String.valueOf(i), 100 + cellWidth * i + cellWidth / 2, 100 - cellHeight, labelStyle);

        }
        for (int i = 0; i < v.length; i++) {
            g.drawString(String.format("%dv, %dw", v[i], w[i]), 100 - cellWidth, 100 + cellHeight * i + cellHeight / 2,
                    labelStyle);
        }
    }
}
