package task_3.ttf_parser;

import task_3.graphics.models.glyphs.Glyph;

import java.io.File;

public class Font {
    private final TTF_Parser parser;

    public Font(File file) {
        parser = new TTF_Parser(file);
    }

    public Glyph getGlyph(char charCode) {
        return parser.getGlyph(charCode);
    }

    public int getUnitsPerEm() {
        return parser.getUnitsPerEm();
    }
    public int getXMin() {
        return parser.getXMin();
    }
    public int getYMin() {
        return parser.getYMin();
    }
    public int getXMax() {
        return parser.getXMax();
    }
    public int getYMax() {
        return parser.getYMax();
    }
}
