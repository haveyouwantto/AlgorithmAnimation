package hywt.algo.animation.gui;

import hywt.algo.animation.AnimationGen;
import hywt.algo.datatype.VideoSize;

import javax.swing.*;
import java.awt.*;

public class GUI extends JFrame {

    public GUI() throws IllegalAccessException, ClassNotFoundException, InstantiationException {
        this("hywt.algo.animation.basic.AnimationTest");
    }

    public GUI(String className) throws IllegalAccessException, InstantiationException, ClassNotFoundException {
        super();
        initialize(className);
    }

    static class Panel extends JPanel {
        private AnimationGen gen;
        private int frame;

        public Panel(AnimationGen gen) {
            this.gen = gen;
        }

        @Override
        protected void paintComponent(Graphics g) {
            //Graphics2D g2 = (Graphics2D) g;
            //g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
            super.paintComponent(g);
            gen.provideFrame(g);
            frame++;
        }

        public int getFrame() {
            return frame;
        }
    }

    private void initialize(String className) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        AnimationGen gen = (AnimationGen) Class.forName(className).newInstance();
        VideoSize size = gen.getSize();

        getContentPane().setPreferredSize(new Dimension(size.width, size.height));
        pack();
        setTitle("算法动画");
        setResizable(false);

        Panel panel = new Panel(gen);
        setContentPane(panel);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        Thread t = new Thread(() -> {
            try {
                while (gen.hasNext()) {
                    panel.repaint();
                    setTitle(String.format("算法动画 - 第%04d帧", panel.getFrame()));
                    Thread.sleep(1000 / 60);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        t.start();
    }

    public static void main(String[] args) {
        GUI gui = null;
        try {
            if (args.length == 0) {
                gui = new GUI();
            } else {
                gui = new GUI(args[0]);
            }
            gui.setVisible(true);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.err.println("未找到此类。");
            e.printStackTrace();
        }
    }
}
