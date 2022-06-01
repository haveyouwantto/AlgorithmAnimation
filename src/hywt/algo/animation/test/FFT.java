package hywt.algo.animation.test;

import hywt.algo.animation.BasicAnimation;
import hywt.algo.datatype.VideoSize;
import hywt.math.function.Mapper;
import hywt.math.shapes.Point2D;
import hywt.math.shapes.Vector2D;

import java.awt.*;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.LinkedList;

public class FFT extends BasicAnimation {
    double tick = 0;

    int width = 1280;
    int height = 720;

    double zoom = 2;
    double speed = 10;
    Mapper mx = new Mapper(-1 / zoom, 1 / zoom, 0, width);
    Mapper my = new Mapper(-1 / zoom / 1.777, 1 / zoom / 1.777, 0, height);


    LinkedList<Point2D> pointList = new LinkedList<>();
    FFTData[] fft;

    Vector2D camera;
    boolean follow = false;
    boolean infiniteLoop = true;

//    Stroke line = new BasicStroke(3);
//    Stroke trail = new BasicStroke(1);


    static class FFTData implements Comparable<FFTData> {
        final double amp;
        final double freq;
        final double phase;

        FFTData(double amp, double freq, double phase) {
            this.amp = amp;
            this.freq = freq;
            this.phase = phase;
        }

        @Override
        public int compareTo(FFTData o) {
            return Double.compare(Math.abs(o.amp), Math.abs(this.amp));
        }
    }

    public FFT() throws IOException {
        super();
        InputStream is = ClassLoader.getSystemResourceAsStream("fft.bin");
        fft = new FFTData[is.available() / 12];
        DataInputStream dis = new DataInputStream(is);
        for (int i = 0; i < fft.length; i++) {
            fft[i] = new FFTData(
                    dis.readFloat(),
                    dis.readFloat(),
                    dis.readFloat()
            );
        }
        Arrays.sort(fft);

        Vector2D center = new Vector2D(0, 0);
        if (follow) {
            for (FFTData data : fft) {
                center.add(Vector2D.fromPolar(data.amp, 2 * Math.PI * tick * data.freq + data.phase));
            }
        }
        camera = center;
    }

    @Override
    public boolean hasNext() {
        return infiniteLoop || tick / fft.length < 1;
    }

    @Override
    public VideoSize getSize() {
        return new VideoSize(width, height);
    }

    @Override
    public void provideFrame(Graphics g) {
        super.provideFrame(g);


        tick += speed;

        Vector2D center = new Vector2D(0, 0);
        for (FFTData data : fft) {
            center.add(Vector2D.fromPolar(data.amp, 2 * Math.PI * tick * data.freq + data.phase));
        }

        if (follow) {
            mx = new Mapper(camera.x - 1 / zoom, camera.x + 1 / zoom, 0, width);
            my = new Mapper(camera.y - 1 / zoom / 1.777, camera.y + 1 / zoom / 1.777, 0, height);
            camera.add(center.clone().subtract(camera).divide(2));
        }
//
//        zoom = 1;
//        speed = 0.0005;

        double cx = 0;
        double cy = 0;
        for (int i = 0; i < fft.length; i++) {
            g.setColor(Color.getHSBColor((float) i / fft.length * 0.75f, 1, 1));

            FFTData data = fft[i];
            Vector2D v = Vector2D.fromPolar(data.amp, 2 * Math.PI * tick * data.freq + data.phase);

            double x = cx + v.x;
            double y = cy + v.y;

            g.drawLine((int) mx.get(cx), (int) my.get(cy), (int) mx.get(x), (int) my.get(y));

            cx = x;
            cy = y;
        }
        pointList.add(new Point2D(center.x, center.y));

        if (tick / fft.length > 1) {
            pointList.removeFirst();
        }
        g.setColor(Color.WHITE);

        Point2D last = null;
        for (Point2D p : pointList) {
            if (last == null) {
                g.drawRect((int) mx.get(p.x), (int) my.get(p.y), 0, 0);
            } else {
                g.drawLine((int) mx.get(last.x), (int) my.get(last.y), (int) mx.get(p.x), (int) my.get(p.y));
            }
            last = p;
        }

        g.setColor(Color.WHITE);
        g.drawString(String.format("%f%%", tick / fft.length * 100), 16, 16);
    }
}
