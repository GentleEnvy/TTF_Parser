package task_3.graphics.models;

import task_3.graphics.Graphic;
import task_3.graphics.canvas.Canvas;
import task_3.graphics.models.glyphs.GraphicGlyph;
import task_3.ttf_parser.Font;

import java.util.ArrayList;
import java.util.List;


public class GraphicString extends Graphic {
    private final String string;
    private final List<GraphicGlyph> glyphs = new ArrayList<>();
    private final Font font;

    private int space = 10;
    private int fontSize = 150;

    public GraphicString(String string, Font font) {
        this.string = string;
        this.font = font;
        for (int index = 0; index < string.length(); ++index) {
            glyphs.add(new GraphicGlyph(font.getGlyph((char) string.codePointAt(index))));
        }
    }

    public String getString() {
        return string;
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }
    public void setSpace(int space) {
        this.space = space;
    }

    public void setRotateOfDegrees(double degreesAngle) {
        getRenderParameters().setDegreesAngle(degreesAngle);
    }
    public void setRotateOfRadians(double radiansAngle) {
        getRenderParameters().setRadiansAngle(radiansAngle);
    }

    @Override
    protected void _render(Canvas canvas) {
        double scale = (double) font.getUnitsPerEm() / fontSize;
        int space = (int) (this.space * scale);
        int offset = 0;

        for (GraphicGlyph graphicGlyph : glyphs) {
            graphicGlyph.getRenderParameters().setScale(scale);
            graphicGlyph.getRenderParameters().setOffsetX(offset);
            graphicGlyph.getRenderParameters().setOffsetY(-font.getYMax());
            graphicGlyph.render(canvas);
            offset += graphicGlyph.getWight() + space;
        }
    }
}
