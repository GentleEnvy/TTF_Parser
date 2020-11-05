package task_3.graphics.models.glyphs;

import task_3.graphics.Graphic;
import task_3.graphics.canvas.Canvas;
import task_3.graphics.models.GraphicLine;
import task_3.graphics.points.Point;


public class GraphicGlyph extends Graphic {
    private final Glyph glyph;

    public GraphicGlyph(Glyph glyph) {
        this.glyph = glyph;
    }

    public int getWight() {
        return glyph.getXMax() - (glyph.getXMin() < 0 ? glyph.getXMin() : 0);
    }

    public int getHeight() {
        return glyph.getYMax() - (glyph.getYMin() < 0 ? glyph.getYMin() : 0);
    }

    @Override
    protected void _render(Canvas canvas) {
        int c = 0;
        boolean isStart = true;

        Point startPoint = new Point(0, 0);
        Point currentPoint = new Point(0, 0);

        for(int p = 0; p < glyph.getPoints().size(); ++p) {
            GlyphPoint glyphPoint = glyph.getPoints().get(p);
            if (isStart) {
                startPoint = new Point(
                        glyphPoint.getPoint().getX(),
                        glyphPoint.getPoint().getY()
                );
                currentPoint = startPoint;
                isStart = false;
            } else {
                new GraphicLine(
                        currentPoint,
                        currentPoint = new Point(
                                glyphPoint.getPoint().getX(),
                                glyphPoint.getPoint().getY()
                        )
                ).render(canvas);
            }

            if (p == glyph.getContourEnds().get(c)) {
                new GraphicLine(
                        startPoint,
                        currentPoint
                ).render(canvas);
                ++c;
                isStart = true;
            }
        }
    }
}
