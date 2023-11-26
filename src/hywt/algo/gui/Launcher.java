package hywt.algo.gui;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Launcher extends JFrame {
    public Launcher() throws IOException {
        super();
        initialize();
    }

    private void initialize() throws IOException {
        Properties prop = new Properties();
        System.out.println(ClassLoader.getSystemResource("classes.properties"));
        InputStream is = ClassLoader.getSystemResourceAsStream("classes.properties");
        prop.load(is);
        System.out.println(prop.entrySet());

        JPanel main = new JPanel();
        main.setLayout(new GridLayout());
        main.setBorder(new TitledBorder("动画列表"));

        JScrollPane scrollPane = new JScrollPane();

        JList<String> classes = new JList<>();
        classes.setListData(prop.stringPropertyNames().toArray(new String[0]));
        scrollPane.setViewportView(classes);

        main.add(scrollPane);
        add(main, BorderLayout.CENTER);

        JButton button = new JButton("播放");
        add(button, BorderLayout.SOUTH);
        button.addActionListener(l -> {
            if (!classes.isSelectionEmpty()) {
                try {
                    GUI gui = new GUI(prop.getProperty(classes.getSelectedValue()));
                    gui.setVisible(true);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });

        setTitle("算法动画");
        getContentPane().setPreferredSize(new Dimension(320, 480));
        pack();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public static void main(String[] args) throws IOException {
        new Launcher();
    }
}
