package hywt.algo.animation.dp;

import java.awt.Color;
import java.awt.Font;

import hywt.algo.animation.AnimationV2;
import hywt.algo.gui.GraphicsV2;
import hywt.algo.gui.TextStyle;

public class Knapsack extends AnimationV2 {

    int[][] dp;
    int[] w;
    int[] v;
    int n;
    int capacity;

    TextStyle gridStyle = TextStyle.create(null, Color.WHITE, "mc");
    TextStyle labelStyle = TextStyle.create(null, Color.YELLOW, "mc");
    TextStyle processStyle = TextStyle.create(new Font("SansSerif", Font.BOLD, 24), Color.CYAN, "tc");
    TextStyle conclusionStyle = TextStyle.create(new Font("SansSerif", Font.BOLD, 24), Color.CYAN, "tl");

    int currentGridX = -1;
    int currentGridY = -1;

    int lookbehindGridX = -1;
    int lookbehindGridY = -1;
    boolean drawPrev = false;

    Color currColor = new Color(64, 0, 0);
    Color prevColor = new Color(0, 32, 64);
    Color takeColor = new Color(64, 64, 0);
    Color currBar = new Color(0, 64, 128);
    Color takeBar = new Color(128, 128, 0);

    String conclusion;

    public Knapsack() {
        int n = 15;
        int capacity = 30;
        int[] w = new int[n];
        int[] v = new int[n];
        for (int i = 0; i < n; i++) {
            w[i] = (int) (Math.random() * 24) + 1;
            v[i] = (int) (Math.random() * 24) + 1;
        }
        init(w, v, capacity);
    }

    public Knapsack(int[] w, int[] v, int capacity) {
        init(w, v, capacity);
    }

    private void init(int[] w, int[] v, int capacity) {
        setSize(854, 480);
        setStatus("背包问题");
        this.w = w;
        this.v = v;
        this.n = w.length;
        this.capacity = capacity;
        // dp[i][j] = max value using items 0..i-1 with capacity j
        // row 0 and col 0 are base cases (all zeros)
        this.dp = new int[n + 1][capacity + 1];
    }

    @Override
    protected void onAlgorithm() throws InterruptedException {
        waitFrames(60);

        // 0-1 背包问题
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= capacity; j++) {
                currentGridX = j;
                currentGridY = i;
                lookbehindGridX = -1;
                lookbehindGridY = -1;
                setStatus("背包问题 | \n当前坐标:(%d,%d)", i, j);
                conclusion = null;
                drawPrev = false;
                waitFrame();

                if (j < w[i - 1]) {
                    // 1. 容量不够，只能不拿
                    int noTake = dp[i - 1][j];
                    waitFrame();

                    // 2. 写入结果
                    conclusion = "没空间拿";
                    waitFrame();
                    dp[i][j] = noTake;
                    waitFrame();
                } else {
                    // 1. 显示不拿的值
                    int noTake = dp[i - 1][j];
                    drawPrev = true;
                    waitFrame();

                    // 2. 显示拿的值（往前找）
                    lookbehindGridX = j - w[i - 1];
                    lookbehindGridY = i - 1;
                    int take = dp[i - 1][j - w[i - 1]] + v[i - 1];
                    waitFrame();

                    // 3. 比较结果，显示大于或小于号
                    conclusion = noTake > take ? "不拿" : "拿";
                    waitFrame();
                    dp[i][j] = Math.max(noTake, take);
                    waitFrame();
                }
            }
        }

        // 清除高亮
        currentGridX = -1;
        currentGridY = -1;
        lookbehindGridX = -1;
        lookbehindGridY = -1;
        setStatus("完成! 最大价值 = %d", dp[n][capacity]);
        waitFrames(120);
        finish();
    }

    @Override
    protected void onDraw(GraphicsV2 g) {
        int cellWidth = 24;
        int cellHeight = 16;
        int startX = 60;
        int startY = 60;

        if (currentGridY > 0 && drawPrev) {
            // "不拿" 的来源：dp[i-1][j]
            g.setColor(prevColor);
            g.fillRect(startX + cellWidth * currentGridX, startY + cellHeight * (currentGridY - 1), cellWidth,
                    cellHeight);
        }

        // Draw the lookbehind region highlight (behind the grid)
        if (lookbehindGridX >= 0 && lookbehindGridY >= 0) {
            // "拿" 的来源：dp[i-1][j-w[i]]
            g.setColor(takeColor);
            g.fillRect(startX + cellWidth * lookbehindGridX, startY + cellHeight * lookbehindGridY, cellWidth,
                    cellHeight);
        }

        // Draw the current cell highlight
        if (currentGridX >= 0 && currentGridY >= 0) {
            g.setColor(currColor);
            g.fillRect(startX + cellWidth * currentGridX, startY + cellHeight * currentGridY, cellWidth, cellHeight);
        }

        // Draw the grid
        g.drawTable(dp, startX, startY, cellWidth, cellHeight, gridStyle, gridStyle);

        // Draw column headers (capacity 0..capacity)
        for (int j = 0; j <= capacity; j++) {
            g.drawString(String.valueOf(j), startX + cellWidth * j + cellWidth / 2, startY - cellHeight / 2,
                    labelStyle);
        }

        // Draw row labels (item info)
        g.drawString("--", startX - cellWidth, startY + cellHeight / 2, labelStyle);
        for (int i = 0; i < n; i++) {
            g.drawString(String.format("%dv,%dw", v[i], w[i]), startX - cellWidth,
                    startY + cellHeight * (i + 1) + cellHeight / 2,
                    labelStyle);
        }

        // Draw process visualization
        int processY = startY + cellHeight * (n + 2) + 100;
        if (currentGridY > 0) {
            int v1 = dp[currentGridY - 1][currentGridX];
            int v2 = currentGridX >= w[currentGridY - 1]
                    ? dp[currentGridY - 1][currentGridX - w[currentGridY - 1]] + v[currentGridY - 1]
                    : 0;
            int avg = (v1 + v2) / 2;

            int height1 = Math.clamp(50 + (v1 - avg) * 3, 5, 100);
            int height2 = Math.clamp(50 + (v2 - avg) * 3, 5, 100);

            if (currentGridX >= 0 && drawPrev) {
                g.drawBar(startX + 200, processY - height1, 80, height1, currBar, null);
                g.drawString(String.valueOf(v1), startX + 240, processY, processStyle);
            }

            if (lookbehindGridX >= 0 && lookbehindGridY >= 0) {
                g.drawBar(startX + 300, processY - height2, 80, height2, takeBar, null);
                g.drawString(String.valueOf(v2), startX + 340, processY, processStyle);
            }

            if (conclusion != null) {
                g.drawString("结论：" + conclusion, startX + 400, processY, conclusionStyle);

                String comparison;
                if (conclusion.equals("拿")) {
                    comparison = "<=";
                } else if (conclusion.equals("不拿")) {
                    comparison = ">=";
                } else {
                    comparison = String.format("%d > %d",
                            w[currentGridY - 1], currentGridX);
                }
                g.drawString(comparison, startX + 280, processY, conclusionStyle);
            }
        }
    }
}
