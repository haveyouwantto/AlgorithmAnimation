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

    // Settings
    int width = 1920;
    int height = 1080;
    double zoom = 0.25;
    boolean follow = false;
    boolean infiniteLoop = false;
    Stroke line = new BasicStroke(2);
    Stroke trail = new BasicStroke(1);


    double ratio = width * 1d / height;

    double speed;
    Mapper mx = new Mapper(-1 / zoom, 1 / zoom, 0, width);
    Mapper my = new Mapper(-1 / zoom / ratio, 1 / zoom / ratio, 0, height);
    Mapper ma = new Mapper(0, 1 / zoom, 0, width);


    LinkedList<Point2D> pointList = new LinkedList<>();
    FFTData[] fft;

    Vector2D camera;


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
            return Double.compare(Math.abs(this.freq), Math.abs(o.freq));
        }
    }

    public FFT() throws IOException {
        super();
        InputStream is = ClassLoader.getSystemResourceAsStream("fft/pi.fft");
        fft = new FFTData[is.available() / 24];
        DataInputStream dis = new DataInputStream(is);
        for (int i = 0; i < fft.length; i++) {
            fft[i] = new FFTData(
                    dis.readDouble(),
                    dis.readDouble() * fft.length,
                    dis.readDouble()
            );
        }
        Arrays.sort(fft);

        //fft[0] = new FFTData(0,0,fft[0].phase);

        Vector2D center = new Vector2D(0, 0);
        if (follow) {
            for (FFTData data : fft) {
                center.add(Vector2D.fromPolar(data.amp, 2 * Math.PI * tick * data.freq + data.phase));
            }
        }
        camera = center;

        // how many frames per cycle
        // speed = 1.0 / frames
        speed = 1.0 / (30 * 60);
    }

    @Override
    public boolean hasNext() {
        return infiniteLoop || tick < 2;
    }

    @Override
    public VideoSize getSize() {
        return new VideoSize(width, height);
    }

    @Override
    public void provideFrame(Graphics g) {
        super.provideFrame(g);

        Graphics2D g2 = (Graphics2D) g;
//        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setFont(g2.getFont().deriveFont(32f));

        Vector2D center = new Vector2D(0, 0);
        Arrays.stream(fft).forEach(data -> center.add(Vector2D.fromPolar(data.amp, 2 * Math.PI * tick * data.freq + data.phase)));

        if (follow) {
//            camera = center;
            mx = new Mapper(camera.x - 1 / zoom, camera.x + 1 / zoom, 0, width);
            my = new Mapper(camera.y - 1 / zoom / 1.777, camera.y + 1 / zoom / 1.777, 0, height);
            ma = new Mapper(0, 1 / zoom, 0, width);

            Vector2D distance = center.clone().subtract(camera);
            camera.add(distance.divide(10));
        }
//
//        if (zoom > 0.01)
//            zoom /= 1.0005;
//        if (speed < 1)
//            speed /= 0.9996;

        double cx = 0;
        double cy = 0;
        for (int i = 0; i < fft.length; i++) {
            // Vector
            g2.setStroke(line);
            g.setColor(Color.getHSBColor((float) Math.cbrt((double) i / fft.length) * 0.75f, 1, 1));

            FFTData data = fft[i];
            Vector2D v = Vector2D.fromPolar(data.amp, 2 * Math.PI * tick * data.freq + data.phase);

            double x = cx + v.x;
            double y = cy + v.y;

            g.drawLine((int) mx.get(cx), (int) my.get(cy), (int) mx.get(x), (int) my.get(y));

            // Circle
            g2.setStroke(trail);
            g.setColor(Color.GRAY);
            double circle = data.amp;
            g.drawOval((int) mx.get(cx - circle), (int) my.get(cy - circle), (int) ma.get(circle), (int) ma.get(circle));

            cx = x;
            cy = y;
        }

        g.setColor(Color.WHITE);
        g2.setStroke(line);
        Point2D last = null;
        for (Point2D p : pointList) {
            if (last != null) {
                g.drawLine((int) mx.get(last.x), (int) my.get(last.y), (int) mx.get(p.x), (int) my.get(p.y));
            }
            last = p;
        }

        pointList.add(new Point2D(center.x, center.y));

        if (tick > 1) {
            pointList.removeFirst();
        }


        g.setColor(Color.WHITE);
        g.drawString(String.format("%f%% | zoom=%5.5g | speed=%5.5g | vectors=%d", tick * 100, zoom, speed, fft.length), 8, 32);

        tick += speed;
    }
}
