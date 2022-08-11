package hywt.algo.datatype;

import java.awt.*;
import java.util.Objects;

public class VideoSize {
    public final int width;
    public final int height;

    public VideoSize(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public Dimension toDimension() {
        return new Dimension(width, height);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VideoSize size = (VideoSize) o;
        return width == size.width &&
                height == size.height;
    }

    @Override
    public int hashCode() {
        return Objects.hash(width, height);
    }
}
