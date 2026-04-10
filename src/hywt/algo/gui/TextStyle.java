package hywt.algo.gui;

import java.awt.*;

/**
 * TextStyle encapsulates text rendering properties.
 * Attributes can be null, in which case the current Graphics state is used.
 */
public class TextStyle {
    private Font font;
    private Color color;
    private String alignment; // e.g., "tl", "mc", "br"

    public TextStyle() {
    }

    public TextStyle(Font font, Color color, String alignment) {
        this.font = font;
        this.color = color;
        this.alignment = validateAlignment(alignment);
    }

    public static TextStyle font(Font font) {
        return new TextStyle(font, null, null);
    }

    public static TextStyle color(Color color) {
        return new TextStyle(null, color, null);
    }

    public static TextStyle align(String alignment) {
        return new TextStyle(null, null, alignment);
    }

    public Font getFont() {
        return font;
    }

    public void setFont(Font font) {
        this.font = font;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public String getAlignment() {
        return alignment;
    }

    public void setAlignment(String alignment) {
        this.alignment = validateAlignment(alignment);
    }

    private String validateAlignment(String alignment) {
        if (alignment == null)
            return null;
        String lower = alignment.toLowerCase();
        if (lower.length() != 2) {
            throw new IllegalArgumentException(
                    "Alignment must be a 2-character code (e.g. 'tl', 'cm', 'br'), got: " + alignment);
        }

        char v = lower.charAt(0);
        char h = lower.charAt(1);

        boolean vValid = (v == 't' || v == 'm' || v == 'b');
        boolean hValid = (h == 'l' || h == 'c' || h == 'r');

        if (!vValid || !hValid) {
            throw new IllegalArgumentException(
                    "Invalid alignment code: " + alignment + ". Use [t|m|b] for vertical and [l|c|r] for horizontal.");
        }

        return lower;
    }

    /**
     * Helper to create a style with all properties.
     */
    public static TextStyle create(Font font, Color color, String alignment) {
        return new TextStyle(font, color, alignment);
    }
}
