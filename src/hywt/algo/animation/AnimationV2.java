package hywt.algo.animation;

import java.awt.*;

/**
 * AnimationV2 is a high-level base class for algorithm animations.
 * It provides a separate thread for the algorithm and synchronizes it with the rendering thread.
 * Use {@link #waitFrame()} to pause the algorithm and wait for a frame to be drawn.
 */
public abstract class AnimationV2 extends BasicAnimation {
    private final Object lock = new Object();
    private Thread algorithmThread;
    
    private volatile boolean started = false;
    private volatile boolean running = false;
    private volatile boolean finished = false;
    private volatile boolean algorithmFinished = false;

    private int tick = 0;
    private int startDelay = 0;
    private String status = "";

    /**
     * The core algorithm logic.
     * Use {@link #waitFrame()} to pause and allow the UI to render.
     */
    protected abstract void onAlgorithm() throws InterruptedException;

    /**
     * Logic for rendering the current state of the algorithm.
     */
    protected abstract void onDraw(Graphics g);

    /**
     * Sets the amount of frames to wait before starting the algorithm.
     */
    public void setStartDelay(int frames) {
        this.startDelay = frames;
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
    protected void drawStatus(Graphics g) {
        if (status != null && !status.isEmpty()) {
            g.setColor(Color.WHITE);
            g.drawString(status, 10, 20);
        }
    }

    @Override
    public void provideFrame(Graphics g) {
        // Clear background if flip is enabled in BasicAnimation
        super.provideFrame(g);

        if (!started) {
            startAlgorithm();
            started = true;
        }

        onDraw(g);
        drawStatus(g);

        tick++;

        if (running) {
            synchronized (lock) {
                lock.notify();
            }
        }
    }

    private synchronized void startAlgorithm() {
        if (algorithmThread != null) return;
        
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
