package task_3.graphics.points;

import javafx.scene.paint.Color;

public class Pixel {
    private final static Color defaultColor = Color.BLACK;

    private final int x;
    private final int y;
    private final Color color;

    public Pixel(int x, int y, Color color) {
        this.x = x;
        this.y = y;
        this.color = color;
    }

    public Pixel(int x, int y) {
        this(x, y, defaultColor);
    }

    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public Color getColor() {
        return color;
    }
}
