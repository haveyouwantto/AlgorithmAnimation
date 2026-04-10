package hywt.algo.animation;

import java.awt.*;

import hywt.algo.datatype.VideoSize;
import hywt.algo.gui.GraphicsV2;
import hywt.algo.gui.TextStyle;

/**
 * AnimationV2 is a high-level base class for algorithm animations.
 * It provides a separate thread for the algorithm and synchronizes it with the
 * rendering thread.
 * Use {@link #waitFrame()} to pause the algorithm and wait for a frame to be
 * drawn.
 */
public abstract class AnimationV2 extends BasicAnimation {
    private final Object lock = new Object();
    private Thread algorithmThread;

    private volatile boolean started = false;
    private volatile boolean running = false;
    private volatile boolean finished = false;
    private volatile boolean algorithmFinished = false;

    protected int width = 640;
    protected int height = 480;
    private int tick = 0;
    private int startDelay = 0;
    private String status = "";

    protected TextStyle defaultTextStyle = TextStyle.create(null, Color.WHITE, "tl");

    /**
     * The core algorithm logic.
     * Use {@link #waitFrame()} to pause and allow the UI to render.
     */
    protected abstract void onAlgorithm() throws InterruptedException;

    /**
     * Logic for rendering the current state of the algorithm.
     */
    protected abstract void onDraw(GraphicsV2 g);

    /**
     * Sets the amount of frames to wait before starting the algorithm.
     */
    public void setStartDelay(int frames) {
        this.startDelay = frames;
    }

    public void setSize(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public void setSize(VideoSize size) {
        this.width = size.width;
        this.height = size.height;
    }

    @Override
    public VideoSize getSize() {
        return new VideoSize(width, height);
    }

    /**
     * Updates the status text displayed on the screen.
     */
    protected void setStatus(String format, Object... args) {
        this.status = String.format(format, args);
    }

    /**
     * Returns the current status text.
     */
    public String getStatus() {
        return status;
    }

    /**
     * Pauses the algorithm thread until the UI thread notifies it.
     */
    protected void waitFrame() throws InterruptedException {
        synchronized (lock) {
            lock.wait();
        }
    }

    /**
     * Pauses the algorithm for a specific number of frames.
     */
    protected void waitFrames(int count) throws InterruptedException {
        for (int i = 0; i < count; i++) {
            waitFrame();
        }
    }

    /**
     * Waits until the specified {@link #startDelay} is reached.
     * Should be called at the beginning of {@link #onAlgorithm()}.
     */
    protected void waitStart() throws InterruptedException {
        while (tick < startDelay) {
            waitFrame();
        }
    }

    /**
     * Draws the status text at the top-left corner by default.
     */
    protected void drawStatus(GraphicsV2 g) {
        if (status != null && !status.isEmpty()) {
            g.drawString(status, 10, 20, defaultTextStyle);
        }
    }

    @Override
    public void provideFrame(Graphics g) {
        // Clear background if flip is enabled in BasicAnimation
        super.provideFrame(g);

        GraphicsV2 gv2 = new GraphicsV2(g);

        if (!started) {
            startAlgorithm();
            started = true;
        }

        onDraw(gv2);
        drawStatus(gv2);

        tick++;

        if (running) {
            synchronized (lock) {
                lock.notify();
            }
        }
    }

    private synchronized void startAlgorithm() {
        if (algorithmThread != null)
            return;

        running = true;
        algorithmThread = new Thread(() -> {
            try {
                onAlgorithm();
            } catch (InterruptedException e) {
                // Interrupted, algorithm should stop
                Thread.currentThread().interrupt();
            } finally {
                running = false;
                algorithmFinished = true;
            }
        }, "AlgorithmThread-" + getClass().getSimpleName());
        algorithmThread.start();
    }

    /**
     * Informative method to signal that the animation is complete.
     */
    public void finish() {
        finished = true;
    }

    @Override
    public boolean hasNext() {
        return !finished;
    }

    public boolean isAlgorithmRunning() {
        return running;
    }

    public boolean isAlgorithmFinished() {
        return algorithmFinished;
    }

    public int getFrameCount() {
        return tick;
    }

    /**
     * Proxy for Thread.sleep, for non-synchronized delays.
     */
    protected void sleep(long ms) throws InterruptedException {
        Thread.sleep(ms);
    }
}
