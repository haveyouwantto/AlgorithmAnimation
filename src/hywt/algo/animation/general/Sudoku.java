package hywt.algo.animation.general;

import hywt.algo.GraphUtils;
import hywt.algo.animation.BasicAnimation;
import hywt.algo.datatype.VideoSize;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

public class Sudoku extends BasicAnimation {

    int width = 640;
    int height = 480;
    long tick = 0;
    long finishTick = Long.MAX_VALUE;
    boolean finished = false;

    // 谜题，0为空白
    int[][] map = {
            {0, 1, 7, 0, 0, 4, 9, 0, 3},
            {3, 0, 0, 8, 0, 6, 5, 0, 0},
            {8, 0, 0, 0, 7, 0, 0, 0, 4},
            {0, 0, 4, 2, 0, 0, 3, 0, 0},
            {1, 0, 2, 0, 9, 5, 4, 0, 6},
            {0, 0, 3, 0, 0, 8, 1, 0, 0},
            {9, 0, 0, 0, 5, 0, 0, 0, 1},
            {0, 0, 1, 6, 0, 2, 0, 0, 9},
            {2, 0, 6, 1, 0, 0, 8, 4, 0}
    };

    int[][] fg = new int[9][9];
    int[][] bg = new int[9][9];

    private final int FG_STATIC = 0;
    private final int FG_PLAYER = 1;

    HashSet<Integer> numbers = new HashSet<>();
    long ns;

    // pointers
    Set<Integer> contains;
    int ptrX = -1;
    int ptrY = -1;
    int selectedPtrX = -1;
    int selectedPtrY = -1;

    int scans = 0;
    int fallback = 0;

    int size = 30;

    public Sudoku() {
        super();

        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                fg[i][j] = map[i][j] == 0 ? FG_PLAYER : FG_STATIC;
            }
        }

        for (int i = 1; i <= 9; i++) numbers.add(i);
    }

    /**
     * 找不重复的数字
     *
     * @param x x坐标
     * @param y y左边
     * @return 不重复的数字列表
     */
    Set<Integer> getPossibleNumbers(int x, int y) throws InterruptedException {
        synchronized (this) {
            HashSet<Integer> contains = (HashSet<Integer>) numbers.clone();
            this.contains = contains;
            int gx = x / 3;
            int gy = y / 3;
            for (int i = 0; i < 9; i++) {
                ptrX = i;
                ptrY = y;
                scans++;
                wait();
                contains.remove(map[y][i]); // 横
            }

            for (int i = 0; i < 9; i++) {
                ptrX = x;
                ptrY = i;
                scans++;
                wait();
                contains.remove(map[i][x]); // 竖
            }
            for (int i = 0; i < 9; i++) {
                ptrX = gx * 3 + i % 3;
                ptrY = gy * 3 + i / 3;
                scans++;
                wait();
                contains.remove(map[gy * 3 + i / 3][gx * 3 + i % 3]); // 九格
            }
        }
        return contains;
    }


    /**
     * 找下一个空白格
     *
     * @param fromX 起始x坐标
     * @param fromY 起始y坐标
     * @return 下一个空白格的坐标，如果找不到返回null
     */
    int[] getNextSpace(int fromX, int fromY) {
        int cx = fromX;
        int cy = fromY;
        do {
            if (map[cy][cx] == 0) return new int[]{cx, cy};
            if (++cx == 9) {
                cx = 0;
                ++cy;
            }
        } while (cy < 9);
        return null;
    }

    /**
     * 尝试解决数独，使用回溯算法
     *
     * @param x x坐标
     * @param y y坐标
     * @return 是否已解决
     */
    boolean solve(int x, int y) throws InterruptedException {
        synchronized (this) {
            selectedPtrX = x;
            selectedPtrY = y;
            wait();
            // 找不重复的数字
            Set<Integer> b = getPossibleNumbers(x, y);
            if (b.isEmpty()) {
                // 如果找不到数字，则返回上一格
                map[y][x] = 0;
                return false;
            }
            boolean solved;
            // 在不重复数字的列表里循环
            for (Integer integer : b) {
                selectedPtrX = x;
                selectedPtrY = y;
                // 试填数字
                map[y][x] = integer;
                this.contains = b;
                wait();
                // 找下一个空白格
                int[] next = getNextSpace(x, y);
                if (next == null) {
                    // 如果没有空白格，则认为已解决
                    return true;
                } else solved = solve(next[0], next[1]); // 否则填下一格
                selectedPtrX = x;
                selectedPtrY = y;
                this.contains = b;
                fallback++;
                wait();
                if (solved) return true;
            }
            // 如果所有数字均不符合条件，则返回上一格
            map[y][x] = 0;
            return false;
        }
    }

    /**
     * 打印数独
     */
    int[] getXY(int x, int y, boolean offset) {
        int offs = offset ? size / 3 : 0;
        return new int[]{(width / 2) - (size * 5) + (x * size) - offs, (height / 2) - (size * 5) + (y * size) + offs / 2};
    }

    void drawRect(Graphics g, int x, int y, Color c) {
        int[] xy = getXY(x, y, true);
        g.setColor(c);
        g.fillRect(xy[0], xy[1], size, size);
    }

    void drawLine(Graphics g) {
        g.setColor(Color.WHITE);
        for (int x = 0; x <= 9; x += 3) {
            int[] xy1 = getXY(x, -1, true);
            int[] xy2 = getXY(x, 8, true);
            g.drawLine(xy1[0], xy1[1], xy2[0], xy2[1]);
        }
        for (int y = 0; y <= 9; y += 3) {
            int[] xy1 = getXY(0, y - 1, true);
            int[] xy2 = getXY(9, y - 1, true);
            g.drawLine(xy1[0], xy1[1], xy2[0], xy2[1]);
        }
    }

    void printMap(Graphics g) {
        drawRect(g, ptrX, ptrY - 1, new Color(25, 69, 213));
        drawRect(g, selectedPtrX, selectedPtrY - 1, new Color(146, 19, 19));
        for (int y = 0; y < 9; y++) {
            for (int x = 0; x < 9; x++) {
                char c = Character.forDigit(map[y][x], 10);
                int[] xy2 = getXY(x, y, false);
                switch (fg[y][x]) {
                    case FG_STATIC:
                        g.setColor(Color.GRAY);
                        break;
                    case FG_PLAYER:
                        g.setColor(Color.CYAN);
                        break;
                }
                if (c != '0') {
                    g.drawChars(new char[]{c}, 0, 1, xy2[0], xy2[1]);
                }
            }
        }

        drawCard(g);
        drawLine(g);
    }

    void drawCard(Graphics g) {
        GraphUtils.drawText(g, "候选: ", 0, height - 32);
        if (contains != null) {
            int x = 64;
            if (contains.isEmpty()) {
                g.setColor(Color.RED);
                GraphUtils.drawText(g, "无候选！", x, height - 32);
            } else for (Integer num : contains) {
                g.drawChars(new char[]{Character.forDigit(num, 10)}, 0, 1, x, height - 32);
                x += 32;
            }
        }
    }

    @Override
    public void provideFrame(Graphics g) {
        super.provideFrame(g);
        g.setFont(g.getFont().deriveFont(24f));
        printMap(g);
        g.setColor(Color.WHITE);
        g.setFont(g.getFont().deriveFont(12f));
        GraphUtils.drawText(g, String.format("回溯法与数独 - scans=%d | fallback=%d", scans, fallback), 0, 16);

        if (tick == 60) {
            Thread thread = new Thread(() -> {
                try {
                    solve(0, 0);
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
            thread.start();
        } else if (tick > 60) {
            synchronized (this) {
                notify();
            }
        }
        tick++;
    }

    private void finish() {
        finished = true;
        finishTick = tick;
    }

    @Override
    public boolean hasNext() {
        return !(finished && tick > finishTick + 60);
    }

    @Override
    public VideoSize getSize() {
        return new VideoSize(width, height);
    }
}
