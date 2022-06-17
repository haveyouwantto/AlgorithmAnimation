package hywt.algo;

import hywt.algo.animation.test.FFT;
import hywt.algo.gui.GUI;

import java.io.IOException;

public class Special {
    public static void main(String[] args) throws IOException {
        GUI gui = new GUI(new FFT());
        gui.setExitOnClose(true);
        gui.setVisible(true);
    }
}
