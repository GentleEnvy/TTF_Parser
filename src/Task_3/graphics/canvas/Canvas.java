package task_3.graphics.canvas;

import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import task_3.graphics.points.Pixel;
import task_3.graphics.points.Point;


public class Canvas extends ImageView {
    private final WritableImage writableImage;
    private final RenderParameters renderParameters = new RenderParameters();

    public Canvas(int screenWeight, int screenHeight) {
        super();
        writableImage = new WritableImage(screenWeight, screenHeight);
        setImage(writableImage);
    }

    public void setPixel(Pixel pixel) {
        try {
            writableImage.getPixelWriter().setColor(
                    pixel.getX(), pixel.getY(), pixel.getColor()
            );
        } catch (IndexOutOfBoundsException e) {
            //
        }
    }

    public RenderParameters addRenderParameters(RenderParameters renderParameters) {
        RenderParameters oldRenderParameters = this.renderParameters.copy();
        this.renderParameters.add(renderParameters);
        return oldRenderParameters;
    }

    public void comeBack(RenderParameters renderParameters) {
        this.renderParameters.copyOf(renderParameters);
    }

    public Point convert(Pixel pixel) {
        return renderParameters.convert(pixel);
    }

    public Pixel convert(Point point) {
        return renderParameters.convert(point);
    }

    public boolean isOut(Pixel pixel) {
        return pixel.getX() < 0 || pixel.getY() < 0 ||
                pixel.getX() > writableImage.getWidth() ||
                pixel.getY() > writableImage.getHeight();
    }

    public boolean isOut(Point point) {
        return isOut(convert(point));
    }
}
