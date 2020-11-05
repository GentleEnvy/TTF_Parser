package task_3.graphics.models.glyphs;

import java.util.ArrayList;
import java.util.List;

public class Glyph {
    private final short numberOfContours;
    private final short xMin;
    private final short yMin;
    private final short xMax;
    private final short yMax;

    private final List<Integer> contourEnds = new ArrayList<>();
    private final List<GlyphPoint> points = new ArrayList<>();

    public Glyph(short numberOfContours, short xMin, short yMin, short xMax, short yMax) {
        this.numberOfContours = numberOfContours;
        this.xMin = xMin;
        this.yMin = yMin;
        this.xMax = xMax;
        this.yMax = yMax;
    }

    public short getNumberOfContours() {
        return numberOfContours;
    }

    public short getXMin() {
        return xMin;
    }

    public short getYMin() {
        return yMin;
    }

    public short getXMax() {
        return xMax;
    }

    public short getYMax() {
        return yMax;
    }

    public List<Integer> getContourEnds() {
        return contourEnds;
    }

    public List<GlyphPoint> getPoints() {
        return points;
    }

    @Override
    public String toString() {
        return "Glyph{" +
                "numberOfContours=" + numberOfContours +
                ", xMin=" + xMin +
                ", yMin=" + yMin +
                ", xMax=" + xMax +
                ", yMax=" + yMax +
                ", contourEnds=" + contourEnds +
                ", points=" + points +
                '}';
    }
}
