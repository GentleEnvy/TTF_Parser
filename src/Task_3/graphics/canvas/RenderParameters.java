package task_3.graphics.canvas;

import task_3.graphics.points.Pixel;
import task_3.graphics.points.Point;

public class RenderParameters {
    private double offsetX = 0;
    private double offsetY = 0;
    private double scaleX = 1;
    private double scaleY = 1;
    private double angle = 0;

    public RenderParameters() {}

    public double getOffsetX() {
        return offsetX;
    }
    public double getOffsetY() {
        return offsetY;
    }
    public double getScaleX() {
        return scaleX;
    }
    public double getScaleY() {
        return scaleY;
    }
    public double getAngle() {
        return angle;
    }

    public void setOffsetX(double offsetX) {
        this.offsetX = offsetX;
    }
    public void setOffsetY(double offsetY) {
        this.offsetY = offsetY;
    }
    public void setScaleX(double scaleX) {
        this.scaleX = scaleX;
    }
    public void setScaleY(double scaleY) {
        this.scaleY = scaleY;
    }
    public void setScale(double scale) {
        setScaleX(scale);
        setScaleY(scale);
    }
    public void setRadiansAngle(double radiansAngle) {
        angle = radiansAngle;
    }
    public void setDegreesAngle(double degreesAngle) {
        setRadiansAngle(degreesAngle * Math.PI / 180);
    }

    public Point convert(Pixel pixel) {
        double x = pixel.getX() * getScaleX() - getOffsetX();
        double y = -(pixel.getY() * getScaleY() - getOffsetY());
        angle = -angle;
        Point convertedPoint = new Point(
                rotateX(x, y),
                rotateY(x, y)
        );
        angle = -angle;
        return convertedPoint;
    }

    public Pixel convert(Point point) {
        point = new Point(
                rotateX(point.getX(), point.getY()),
                rotateY(point.getX(), point.getY())
        );
//        double rotatedOffsetX = rotateX(getOffsetX(), getOffsetY());
//        double rotatedOffsetY = rotateY(getOffsetX(), getOffsetY());

//        double x = (point.getX() + rotatedOffsetX) / getScaleX();
//        double y = -(point.getY() + rotatedOffsetY) / getScaleY();

        double x = (point.getX() + getOffsetX()) / getScaleX();
        double y = -(point.getY() + getOffsetY()) / getScaleY();

        return new Pixel((int) x, (int) y);
    }

    private double rotateX(double x, double y) {
        return x * Math.cos(angle) - y * Math.sin(angle);
    }

    private double rotateY(double x, double y) {
        return x * Math.sin(angle) + y * Math.cos(angle);
    }

    public void add(RenderParameters renderParameters) {
        angle += renderParameters.getAngle();
        offsetX += rotateX(renderParameters.getOffsetX(), renderParameters.getOffsetY());
        offsetY += rotateY(renderParameters.getOffsetX(), renderParameters.getOffsetY());
        scaleX *= renderParameters.getScaleX();
        scaleY *= renderParameters.getScaleY();

    }

    public RenderParameters copy() {
        RenderParameters copyRenderParameters = new RenderParameters();
        copyRenderParameters.offsetX = offsetX;
        copyRenderParameters.offsetY = offsetY;
        copyRenderParameters.scaleX = scaleX;
        copyRenderParameters.scaleY = scaleY;
        copyRenderParameters.angle = angle;
        return copyRenderParameters;
    }

    public void copyOf(RenderParameters renderParameters) {
        if (renderParameters == null) {
            return;
        }

        this.offsetX = renderParameters.getOffsetX();
        this.offsetY = renderParameters.getOffsetY();
        this.scaleX = renderParameters.getScaleX();
        this.scaleY = renderParameters.getScaleY();
        this.angle = renderParameters.getAngle();
    }

    @Override
    public String toString() {
        return "RenderParameters{" +
                "offsetX=" + offsetX +
                ", offsetY=" + offsetY +
                ", scaleX=" + scaleX +
                ", scaleY=" + scaleY +
                ", angle=" + angle +
                '}';
    }
}
