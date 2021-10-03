package hywt.algo.animation.gui;

import hywt.algo.animation.AnimationGen;
import hywt.algo.animation.basic.AnimationTest;
import hywt.algo.animation.sorting.MergeSort;
import hywt.algo.animation.sorting.SlowSort;
import hywt.algo.datatype.VideoSize;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUI extends JFrame {
    public GUI(){
        super();
        initialize();
    }

    class Panel extends JPanel{
        private AnimationGen gen;
        public Panel(AnimationGen gen){
            this.gen = gen;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            gen.provideFrame(g);
        }
    }

    private void initialize(){
        AnimationGen gen = new SlowSort(1);
        VideoSize size = gen.getSize();

        getContentPane().setPreferredSize(new Dimension(size.width,size.height));
        pack();
        setTitle("算法动画");
        setResizable(false);

        JPanel panel = new Panel(gen);
        setContentPane(panel);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        final int[] i = {0};
        Timer t = new Timer(1000 / 60, e -> {
            panel.repaint();
            setTitle(String.format("算法动画 - 第%04d帧", i[0]++));
        });
        t.start();
    }

    public static void main(String[] args) {
        GUI gui = new GUI();
        gui.setVisible(true);
    }
}
