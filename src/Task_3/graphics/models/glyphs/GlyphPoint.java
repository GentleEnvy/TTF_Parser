package task_3.graphics.models.glyphs;


import task_3.graphics.points.Point;


public class GlyphPoint {
    private final Point point;
    private final boolean isCurve;

    public GlyphPoint(int x, int y, boolean isCurve) {
        this(new Point(x, y), isCurve);
    }

    public GlyphPoint(Point point, boolean isCurve) {
        this.point = point;
        this.isCurve = isCurve;
    }

    public Point getPoint() {
        return point;
    }
    public boolean isCurve() {
        return isCurve;
    }
}
