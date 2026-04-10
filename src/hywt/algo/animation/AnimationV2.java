package hywt.algo.animation;

import java.awt.*;

import hywt.algo.datatype.VideoSize;
import hywt.algo.gui.GraphicsV2;
import hywt.algo.gui.TextStyle;

/**
 * AnimationV2 is a simplified base class for algorithm animations.
 * Uses a basic handshake to synchronize algorithm and rendering threads.
 */
public abstract class AnimationV2 extends BasicAnimation {
    private final Object lock = new Object();
    private Thread algorithmThread;

    private volatile boolean started = false;
    private volatile boolean running = false;
    private volatile boolean finished = false;
    private volatile boolean algorithmFinished = false;

    // Minimal state for the handshake
    private boolean algorithmTurn = false;

    protected int width = 640;
    protected int height = 480;
    private int tick = 0;
    private int startDelay = 0;
    private String status = "";

    protected TextStyle defaultTextStyle = TextStyle.create(null, Color.WHITE, "tl");

    protected abstract void onAlgorithm() throws InterruptedException;
    protected abstract void onDraw(GraphicsV2 g);

    public void setStartDelay(int frames) { this.startDelay = frames; }
    public void setSize(int width, int height) { this.width = width; this.height = height; }
    public void setSize(VideoSize size) { this.width = size.width; this.height = size.height; }
    
    @Override
    public VideoSize getSize() { return new VideoSize(width, height); }

    protected void setStatus(String format, Object... args) {
        this.status = String.format(format, args);
    }

    public String getStatus() { return status; }

    /**
     * Minimal handshake: Finish turn, notify UI, and wait for next turn.
     */
    protected void waitFrame() throws InterruptedException {
        synchronized (lock) {
            algorithmTurn = false;
            lock.notifyAll();
            while (!algorithmTurn && running && !finished) {
                lock.wait();
            }
        }
    }

    protected void waitFrames(int count) throws InterruptedException {
        for (int i = 0; i < count; i++) waitFrame();
    }

    protected void waitStart() throws InterruptedException {
        while (tick < startDelay) waitFrame();
    }

    protected void drawStatus(GraphicsV2 g) {
        if (status != null && !status.isEmpty()) {
            g.drawString(status, 10, 20, defaultTextStyle);
        }
    }

    @Override
    public void provideFrame(Graphics g) {
        super.provideFrame(g);
        GraphicsV2 gv2 = new GraphicsV2(g);

        if (!started) {
            startAlgorithm();
            started = true;
        }

        synchronized (lock) {
            // Give turn to algorithm
            algorithmTurn = true;
            lock.notifyAll();
            
            // Wait for algorithm to finish its turn (step)
            try {
                if (running && !algorithmFinished) {
                    while (algorithmTurn && running && !algorithmFinished) {
                        lock.wait(100); // Small timeout to prevent total hang
                    }
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        onDraw(gv2);
        drawStatus(gv2);
        tick++;
    }

    private synchronized void startAlgorithm() {
        if (algorithmThread != null) return;
        running = true;
        algorithmThread = new Thread(() -> {
            try {
                onAlgorithm();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                synchronized (lock) {
                    running = false;
                    algorithmFinished = true;
                    algorithmTurn = false;
                    lock.notifyAll();
                }
            }
        }, "AlgorithmThread-" + getClass().getSimpleName());
        algorithmThread.start();
    }

    public void finish() {
        synchronized (lock) {
            finished = true;
            algorithmTurn = true; // Release any waiting algorithm thread
            lock.notifyAll();
        }
    }

    @Override
    public boolean hasNext() { return !finished; }
}
