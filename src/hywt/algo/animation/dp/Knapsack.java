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

    int currentGridX = -1;
    int currentGridY = -1;

    int lookbehindGridX = -1;
    int lookbehindGridY = -1;

    volatile String processLine1 = "";
    volatile String processLine2 = "";
    volatile String processLine3 = "";

    Color currColor = new Color(64, 0, 0);
    Color prevColor = new Color(0, 32, 64);
    Color takeColor = new Color(64, 64, 0);
    Color backRegion = new Color(32, 32, 32);

    public Knapsack() {
        int n = 15;
        int capacity = 20;
        int[] w = new int[n];
        int[] v = new int[n];
        for (int i = 0; i < n; i++) {
            w[i] = (int) (Math.random() * 10) + 1;
            v[i] = (int) (Math.random() * 10) + 1;
        }
        init(w, v, capacity);
        setSize(854, 480);
    }

    public Knapsack(int[] w, int[] v, int capacity) {
        init(w, v, capacity);
        setSize(640, 480);
    }

    private void init(int[] w, int[] v, int capacity) {
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

                if (j < w[i - 1]) {
                    // 帧1: 容量不够，只能不拿
                    int noTake = dp[i - 1][j];
                    processLine1 = String.format("dp[%d][%d]: 容量%d < 重量%d，不拿", i, j, j, w[i - 1]);
                    processLine2 = String.format("不拿 = dp[%d][%d] = %d", i - 1, j, noTake);
                    processLine3 = "";
                    waitFrame();

                    // 帧2: 写入结果
                    dp[i][j] = noTake;
                    processLine3 = String.format("dp[%d][%d] = %d", i, j, noTake);
                    waitFrame();
                } else {
                    // 帧1: 显示不拿的值
                    int noTake = dp[i - 1][j];
                    processLine1 = String.format("dp[%d][%d]: 不拿 = dp[%d][%d] = %d", i, j, i - 1, j, noTake);
                    processLine2 = "";
                    processLine3 = "";
                    waitFrame();

                    // 帧2: 显示拿的值（往前找）
                    lookbehindGridX = j - w[i - 1];
                    lookbehindGridY = i - 1;
                    int take = dp[i - 1][j - w[i - 1]] + v[i - 1];
                    processLine2 = String.format("拿 = dp[%d][%d] + v%d = %d + %d = %d",
                            i - 1, j - w[i - 1], i, dp[i - 1][j - w[i - 1]], v[i - 1], take);
                    waitFrame();

                    // 帧3: 比较结果，显示大于或小于号
                    dp[i][j] = Math.max(noTake, take);
                    String cmp = noTake > take ? ">" : noTake < take ? "<" : "=";
                    String winner = noTake >= take ? "不拿" : "拿";
                    processLine3 = String.format("%d %s %d → %s, dp[%d][%d] = %d",
                            noTake, cmp, take, winner, i, j, dp[i][j]);
                    waitFrame();
                }
            }
        }

        // 清除高亮
        currentGridX = -1;
        currentGridY = -1;
        lookbehindGridX = -1;
        lookbehindGridY = -1;
        processLine1 = "";
        processLine2 = "";
        processLine3 = String.format("完成! 最大价值 = %d", dp[n][capacity]);
        setStatus("完成! 最大价值 = %d", dp[n][capacity]);
        waitFrames(120);
        finish();
    }

    @Override
    protected void onDraw(GraphicsV2 g) {
        int cellWidth = 32;
        int cellHeight = 16;
        int startX = 100;
        int startY = 60;

        // Draw the lookbehind region highlight (behind the grid)
        if (lookbehindGridX >= 0 && lookbehindGridY >= 0) {
            // "不拿" 的来源：dp[i-1][j]
            g.setColor(prevColor);
            g.fillRect(startX + cellWidth * currentGridX, startY + cellHeight * lookbehindGridY, cellWidth, cellHeight);
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

        // Draw process explanation (large font, at bottom)
        int processY = startY + cellHeight * (n + 2) + 20;
        if (!processLine1.isEmpty()) {
            g.drawString(processLine1, width / 2, processY, processStyle);
        }
        if (!processLine2.isEmpty()) {
            g.drawString(processLine2, width / 2, processY + 32, processStyle);
        }
        if (!processLine3.isEmpty()) {
            g.drawString(processLine3, width / 2, processY + 64, processStyle);
        }
    }
}
