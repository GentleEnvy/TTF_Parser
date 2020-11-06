package task_3.window;

import javafx.scene.layout.Pane;
import task_3.graphics.Graphic;
import task_3.graphics.canvas.Canvas;
import task_3.graphics.canvas.RenderParameters;
import task_3.graphics.points.Pixel;
import task_3.graphics.points.Point;

import java.util.ArrayList;
import java.util.List;


public class PixelPane extends Pane {
    private static final int DEFAULT_SCREEN_WIGHT = 1920;
    private static final int DEFAULT_SCREEN_HEIGHT = 1080;

    private static final double SPEED_DRAG = 10;
    private static final double SPEED_SCALE = 0.03;

    private final List<Graphic> graphics = new ArrayList<>();
    private final RenderParameters renderParameters = new RenderParameters();
    {
        renderParameters.setOffsetX(4500);
        renderParameters.setOffsetY(-4000);
    }

    private Pixel prevDrag = null;
    {
        setOnMousePressed(
                event -> prevDrag = new Pixel(
                        (int) event.getX(),
                        (int) event.getY()
                )
        );

        setOnMouseDragged(
                event -> {
                    Pixel delta;
                    Pixel current = new Pixel((int) event.getX(), (int) event.getY());

                    if (prevDrag != null) {
                        delta = new Pixel(
                                (int) ((current.getX() - prevDrag.getX()) * SPEED_DRAG),
                                (int) ((current.getY() - prevDrag.getY()) * SPEED_DRAG)
                        );

                        Point deltaReal = renderParameters.convert(delta);
                        Point zeroReal = renderParameters.convert(new Pixel(0, 0));
                        Point vector = new Point(
                                zeroReal.getX() - deltaReal.getX(),
                                zeroReal.getY() - deltaReal.getY()
                        );
                        renderParameters.setOffsetX(
                                renderParameters.getOffsetX() - vector.getX()
                        );
                        renderParameters.setOffsetY(
                                renderParameters.getOffsetY() - vector.getY()
                        );
                        prevDrag = current;
                    }
                    render();
                }
        );

        setOnMouseReleased(event -> prevDrag = null);

        setOnScroll(event -> {
            int ticks = (int) -event.getDeltaY() / 32;
            double k = ticks <= 0 ?  1 - SPEED_SCALE : 1 + SPEED_SCALE;
            RenderParameters scaleParameters = new RenderParameters();
            scaleParameters.setScale(k * Math.pow(k, Math.abs(ticks)));
            renderParameters.add(scaleParameters);
            render();
        });
    }

    public void addGraphic(Graphic graphic) {
        graphics.add(graphic);
        render();
    }

    public void removeGraphic(Graphic graphic) {
        graphics.remove(graphic);
        render();
    }

    public void render() {
        getChildren().clear();

        Canvas canvas = new Canvas(
                getWidth() <= 0 ? DEFAULT_SCREEN_WIGHT : (int) getWidth(),
                getHeight() <= 0 ? DEFAULT_SCREEN_HEIGHT : (int) getHeight()
        );
        canvas.addRenderParameters(renderParameters);

        for (Graphic graphic : graphics) {
            graphic.render(canvas);
        }

        getChildren().add(canvas);
    }
}
