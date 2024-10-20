package hywt.algo.gui;

import hywt.algo.animation.AnimationGen;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Set;

public class Launcher extends JFrame {
    public Launcher() throws IOException {
        super();
        initialize();
    }

    private void initialize() throws IOException {
        setTitle("算法动画");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setPreferredSize(new Dimension(640, 480));

        // Set up the main panel that can scroll
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS)); // Vertical layout for categories

        for (Map.Entry<String, Set<Class<? extends AnimationGen>>> category : Registry.registry.entrySet()) {
            JPanel panel = new JPanel();
            panel.setBorder(new TitledBorder(category.getKey()));
            panel.setPreferredSize(new Dimension(300, panel.getPreferredSize().height));
            panel.setLayout(new FlowLayout()); // Horizontal layout for buttons

            for (Class<? extends AnimationGen> animationClass : category.getValue()) {
                JButton button = new JButton(animationClass.getSimpleName());
                button.addActionListener(e -> {
                    try {
                        Constructor<? extends AnimationGen> constructor = animationClass.getConstructor();
                        GUI gui = new GUI(constructor.newInstance());
                        gui.setVisible(true);
                    } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException ex) {
                        // Show a message dialog for reflective errors
                        JOptionPane.showMessageDialog(this, "Error instantiating: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                });
                panel.add(button);
            }
            mainPanel.add(panel);
        }

        // Add mainPanel to the JScrollPane
        JScrollPane pane = new JScrollPane(mainPanel);
        pane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        getContentPane().add(pane, BorderLayout.CENTER);

        pack(); // Call pack() after adding components
        setVisible(true); // Set visible at the end
    }

    public static void main(String[] args) {
        // Run on Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            try {
                new Launcher();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}