package hywt.algo.animation.plot;

import hywt.math.shapes.Vector2D;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class Clock extends Plot {
    int tick = 0;

    @Override
    public boolean hasNext() {
        return true;
    }

    @Override
    public void provideFrame(Graphics g) {
        super.provideFrame(g);
        Graphics2D g2 = (Graphics2D) g;
        tick++;
        clear();
        Calendar calendar = new GregorianCalendar();
        double seconds = calendar.get(Calendar.MILLISECOND) / 1000d + calendar.get(Calendar.SECOND);
        double minutes = seconds / 60 + calendar.get(Calendar.MINUTE);
        double hours = minutes / 60 + calendar.get(Calendar.HOUR_OF_DAY);
        Vector2D secV = Vector2D.fromPolar(1.2, -Math.toRadians(seconds * 6 - 90));
        addLine(0, 0, secV.x, secV.y, Color.RED);
        Vector2D minV = Vector2D.fromPolar(1.2, -Math.toRadians(minutes * 6 - 90));
        addLine(0, 0, minV.x, minV.y, Color.GREEN);
        Vector2D hourV = Vector2D.fromPolar(1.2, -Math.toRadians(hours * 30 - 90));
        addLine(0, 0, hourV.x, hourV.y, Color.BLUE);

        g.setColor(Color.WHITE);
        g.drawOval((int) getPosX(-1.2), (int) getPosY(1.2), (int) getScaleX(1.2), (int) getScaleY(1.2));

        String date = String.format("%04d-%02d-%02d", calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        Rectangle2D rect = g.getFont().getStringBounds(date, g2.getFontRenderContext());
        g.drawChars(date.toCharArray(), 0, date.length(), (int) (250 - rect.getWidth() / 2d), 450);

        String time = String.format("%02d:%02d:%06.03f", calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), seconds);
        rect = g.getFont().getStringBounds(time, g2.getFontRenderContext());
        g.drawChars(time.toCharArray(), 0, time.length(), (int) (250 - rect.getWidth() / 2d), (int) (450 + rect.getHeight()));
    }
}
