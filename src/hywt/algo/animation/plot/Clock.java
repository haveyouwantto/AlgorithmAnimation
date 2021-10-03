package hywt.algo.animation.plot;

import hywt.math.shapes.Vector2D;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.Calendar;
import java.util.Date;
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
        tick++;
        clear();
        Calendar calendar = new GregorianCalendar();
        double seconds = calendar.get(Calendar.MILLISECOND) / 1000d + calendar.get(Calendar.SECOND);
        double minutes = seconds / 60 + calendar.get(Calendar.MINUTE);
        double hours = minutes / 60 + calendar.get(Calendar.HOUR_OF_DAY);
        Vector2D secV = Vector2D.fromPolar(1, -Math.toRadians(seconds * 6 - 90));
        addLine(0, 0, secV.x, secV.y);
        Vector2D minV = Vector2D.fromPolar(1, -Math.toRadians(minutes * 6 - 90));
        addLine(0, 0, minV.x, minV.y);
        Vector2D hourV = Vector2D.fromPolar(1, -Math.toRadians(hours * 30 - 90));
        addLine(0, 0, hourV.x, hourV.y);
        String text = String.format("%02d:%02d:%02.02f", calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), seconds);
        //Rectangle2D rect = g.getFont().getStringBounds(text, g.getFont());
        g.drawChars(text.toCharArray(), 0, text.length(), (int) (250 - 0 / 2d), 375);
    }
}
