package hywt.algo.animation.general;

import hywt.algo.animation.BasicAnimation;
import hywt.algo.datatype.VideoSize;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

public class Sudoku extends BasicAnimation {

    int width = 640;
    int height = 480;

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

    public Sudoku() {
        super();

        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                fg[i][j] = map[i][j] == 0 ? FG_PLAYER : FG_STATIC;
            }
        }
    }

    /**
     * 找不重复的数字
     *
     * @param x x坐标
     * @param y y左边
     * @return 不重复的数字列表
     */
    Set<Integer> getPossibleNumbers(int x, int y) {
        HashSet<Integer> contains = (HashSet<Integer>) numbers.clone();
        int gx = x / 3;
        int gy = y / 3;
        for (int i = 0; i < 9; i++) {
            contains.remove(map[y][i]); // 横
            contains.remove(map[i][x]); // 竖
            contains.remove(map[gy * 3 + i / 3][gx * 3 + i % 3]); // 九格
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
    boolean solve(int x, int y) {
        // 找不重复的数字
        Set<Integer> b = getPossibleNumbers(x, y);
        if (b.size() == 0) {
            // 如果找不到数字，则返回上一格
            map[y][x] = 0;
            return false;
        }
        boolean solved;
        // 在不重复数字的列表里循环
        for (Integer integer : b) {
            // 试填数字
            map[y][x] = integer;
            // 找下一个空白格
            int[] next = getNextSpace(x, y);
            if (next == null) {
                // 如果没有空白格，则认为已解决
                return true;
            } else solved = solve(next[0], next[1]); // 否则填下一格
            if (solved) return true;
        }
        // 如果所有数字均不符合条件，则返回上一格
        map[y][x] = 0;
        return false;
    }

    /**
     * 打印数独
     */
    void printMap(Graphics g) {
        for (int y = 0; y < 9; y++) {
            for (int x = 0; x < 9; x++) {
                char c = Character.forDigit(map[y][x], 10);
                int x2 = (width / 2) - (24 * 5) + (x * 24);
                int y2 = (height / 2) - (24 * 5) + (y * 24);
                switch (fg[y][x]){
                    case FG_STATIC:
                        g.setColor(Color.GRAY);
                        break;
                    case FG_PLAYER:
                        g.setColor(Color.CYAN);
                        break;
                }
                if (c != '0') {
                    g.drawChars(new char[]{c}, 0, 1, x2, y2);
                }
            }
        }
    }

    @Override
    public void provideFrame(Graphics g) {
        super.provideFrame(g);
        g.setFont(g.getFont().deriveFont(20f));
        printMap(g);
    }

    @Override
    public boolean hasNext() {
        return false;
    }

    @Override
    public VideoSize getSize() {
        return new VideoSize(width, height);
    }
}
