package hywt.algo.gui;

import hywt.algo.animation.AnimationGen;
import hywt.algo.datatype.VideoSize;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class GUI extends JFrame {

    public GUI() throws IllegalAccessException, ClassNotFoundException, InstantiationException {
        this("hywt.algo.animation.basic.AnimationTest");
    }

    public GUI(String className) throws IllegalAccessException, InstantiationException, ClassNotFoundException {
        super();
        initialize(className);
    }

    public GUI(AnimationGen gen) {
        super();
        initialize(gen);
    }

    static class Panel extends JPanel {
        private AnimationGen gen;
        private int frame = 1;
        private BufferedImage buffer;

        public Panel(AnimationGen gen) {
            this.gen = gen;
            setBackground(Color.BLACK);
            VideoSize size = gen.getSize();
            buffer = new BufferedImage(size.width, size.height, BufferedImage.TYPE_3BYTE_BGR);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            //Graphics2D g2 = (Graphics2D) g;
            //g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
            gen.provideFrame(buffer.getGraphics());
            g.drawImage(buffer, 0, 0, null);
            frame++;
        }

        public int getFrame() {
            return frame;
        }
    }

    private void initialize(AnimationGen gen) {
        VideoSize size = gen.getSize();

        getContentPane().setPreferredSize(new Dimension(size.width, size.height));
        pack();
        setTitle("算法动画");
        setResizable(false);

        Panel panel = new Panel(gen);
        setContentPane(panel);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        //setType(Type.POPUP);

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

    private void initialize(String className) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        AnimationGen gen = (AnimationGen) Class.forName(className).newInstance();
        initialize(gen);
    }

    public void setExitOnClose(boolean exitOnClose) {
        if (exitOnClose)
            setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        else
            setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    }

    public static void main(String[] args) {
        GUI gui = null;
        try {
            if (args.length == 0) {
                gui = new GUI();
            } else {
                gui = new GUI(args[0]);
            }
            gui.setExitOnClose(true);
            gui.setVisible(true);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
            System.err.println("这个类不是算法动画。");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.err.println("未找到此类。");
        } catch (ClassCastException e) {
            e.printStackTrace();
            System.err.println("这个类不是算法动画。");
        }
    }
}
