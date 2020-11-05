package task_3.graphics.models;

import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;
import task_3.graphics.Graphic;
import task_3.graphics.canvas.Canvas;
import task_3.graphics.points.Pixel;
import task_3.graphics.points.Point;


public class GraphicLine extends Graphic {
    private static final Color defaultColor = Color.BLACK;

    private final Point start;
    private final Point end;
    private final Color color;

    public GraphicLine(Point start, Point end, Color color) {
        this.start = start;
        this.end = end;
        this.color = color;
    }

    public GraphicLine(Point start, Point end) {
        this(start, end, defaultColor);
    }

    @Override
    protected void _render(Canvas canvas) {
        Pixel startPixel = canvas.convert(start);
        Pixel endPixel = canvas.convert(end);

        if (canvas.isOut(startPixel) && canvas.isOut(endPixel)) {
            return;
        }

        int startX = startPixel.getX();
        int startY = startPixel.getY();
        int endX = endPixel.getX();
        int endY = endPixel.getY();

        boolean isSteep = Math.abs(startX - endX) < Math.abs(startY - endY);
        if (isSteep) {
            int startX_ = startX;
            //noinspection SuspiciousNameCombination
            startX = startY;
            //noinspection SuspiciousNameCombination
            startY = startX_;

            int endX_ = endX;
            //noinspection SuspiciousNameCombination
            endX = endY;
            //noinspection SuspiciousNameCombination
            endY = endX_;
        }

        if (startX > endX) {
            int startX_ = startX;
            startX = endX;
            endX = startX_;
            int startY_ = startY;
            startY = endY;
            endY = startY_;
        }

        int dx = endX - startX;
        double dy = endY - startY;

        try {
            canvas.setPixel(
                    new Pixel(
                            isSteep ? startY : startX,
                            isSteep ? startX : startY,
                            color
                    )
            );
            canvas.setPixel(
                    new Pixel(
                            isSteep ? endY : endX,
                            isSteep ? endX : endY,
                            color
                    )
            );
        } catch (IllegalArgumentException e) {
            //
        }

        double k = dy / dx;
        double y = startY + k;
        for (int x = startX + 1; x < endX; ++x) {
            int intY = (int) y;
            try {
                canvas.setPixel(
                        new Pixel(
                                isSteep ? intY : x,
                                isSteep ? x : intY,
                                createColor(1 - (y - intY))
                        )
                );
                canvas.setPixel(
                        new Pixel(
                                isSteep ? intY + 1 : x,
                                isSteep ? x : intY + 1,
                                createColor(y - intY)
                        )
                );
            } catch (IllegalArgumentException e) {
                //
            }
            y += k;
        }
    }

    private Color createColor(double opacity) {
        return new Color(
                color.getRed(),
                color.getGreen(),
                color.getBlue(),
                opacity
        );
    }
}
