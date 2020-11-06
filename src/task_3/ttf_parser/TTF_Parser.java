package task_3.ttf_parser;

import task_3.graphics.models.glyphs.Glyph;
import task_3.graphics.models.glyphs.GlyphPoint;

import java.io.File;
import java.util.*;


class TTF_Parser {
    private static final int ON_CURVE = 1;
    private static final int X_IS_BYTE = 2;
    private static final int Y_IS_BYTE = 4;
    private static final int REPEAT = 8;
    private static final int X_DELTA = 16;
    private static final int Y_DELTA = 32;

    private static final int ARG_1_AND_2_ARE_WORDS = 1;
    private static final int WE_HAVE_A_SCALE = 8;
    private static final int MORE_COMPONENTS = 32;
    private static final int WE_HAVE_AN_X_AND_Y_SCALE = 64;
    private static final int WE_HAVE_A_TWO_BY_TWO = 128;
    private static final int WE_HAVE_INSTRUCTIONS = 256;

    private static final Glyph SPACE = new Glyph(
            (short) 0,
            (short) 0, (short) 0,
            (short) 500, (short) 500
    );
    private static final Glyph UNKNOWN = SPACE;

    private final BinaryReader file;
    private final Map<String, HeadTable> headTables;
    private final int length;

    private int unitsPerEm;
    private int xMin;
    private int yMin;
    private int xMax;
    private int yMax;
    private int indexToLocFormat;

    private final CMap cmap;

    public TTF_Parser(File file) {
        this.file = new BinaryReader(file);
        this.headTables = readOffsetTables();
        this.readHead();
        this.cmap = readCmap();
        this.length = glyphCount();
    }

    public int getUnitsPerEm() {
        return unitsPerEm;
    }
    public int getXMin() {
        return xMin;
    }
    public int getYMin() {
        return yMin;
    }
    public int getXMax() {
        return xMax;
    }
    public int getYMax() {
        return yMax;
    }

    private Map<String, HeadTable> readOffsetTables() {
        Map<String, HeadTable> tables = new HashMap<>();
        file.getUint32();  // scalarType
        int numTables = file.getUint16();
        file.getUint16();  // searchRange
        file.getUint16();  // entrySelector
        file.getUint16();  // rangeShift

        for (int i = 0; i < numTables; ++i) {
            String tag = file.getString(4);
            tables.put(
                    tag,
                    new HeadTable(
                            file.getUint32(),
                            file.getUint32(),
                            file.getUint32()
                    )
            );
        }
        return tables;
    }

    private CMap readCmap() {
        long cmapOffset = headTables.get("cmap").getOffset();

        long oldPosition = file.seek(cmapOffset);

        file.getUint16(); // version - must be 0
        file.getUint16();  // number subtables

        file.getUint16();  // platformID
        file.getUint16();  // platformSpecificID
        long offset = file.getUint32();

        file.seek(cmapOffset + offset);
        CMap cmap = new CMap(file);

        file.seek(oldPosition);

        return cmap;
    }

    private void readHead() {
        file.seek(headTables.get("head").getOffset());

        file.getFixed();  // version
        file.getFixed();  // fontRevision
        file.getUint32();  // checksumAdjustment
        file.getUint32();  // magicNumber
        file.getUint16();  // flags
        unitsPerEm = file.getUint16();
        file.getDate();  // created
        file.getDate();  // modified
        this.xMin = file.getFWord();
        this.yMin = file.getFWord();
        this.xMax = file.getFWord();
        this.yMax = file.getFWord();
        file.getUint16();  // macStyle
        file.getUint16();  // lowestRecPPEM
        file.getInt16();  // fontDirectionHint
        this.indexToLocFormat = file.getInt16();
        file.getInt16();  // glyphDataFormat
    }

    private int glyphCount() {
        long oldPosition = file.seek(headTables.get("maxp").getOffset() + 4);
        int count = file.getUint16();
        file.seek(oldPosition);
        return count;
    }

    private long getGlyphOffset(int index) {
        HeadTable loca = headTables.get("loca");
        long offset, old;

        if (indexToLocFormat == 1) {
            old = file.seek(loca.getOffset() + index * 4);
            offset = file.getUint32();
        } else {
            old = file.seek(loca.getOffset() + index * 2);
            offset = file.getUint16() * 2;
        }

        file.seek(old);

        return offset + headTables.get("glyf").getOffset();
    }

    private Glyph readGlyph(int index) {
        if (index < 0 || index > length) {
            return null;
        }

        long offset = getGlyphOffset(index);

        if (offset >= headTables.get("glyf").getOffset() +
                headTables.get("glyf").getLength()) {
            return null;
        }

        file.seek(offset);

        short numberOfContours = file.getInt16();

        Glyph glyph = new Glyph(
                numberOfContours,
                file.getFWord(),
                file.getFWord(),
                file.getFWord(),
                file.getFWord()
        );

        if (glyph.getNumberOfContours() == -1) {
            return readGlyph(readCompoundGlyph());
        } else {
            readSimpleGlyph(glyph);
        }

        return glyph;
    }

    private void readSimpleGlyph(Glyph glyph) {
        for (int i = 0; i < glyph.getNumberOfContours(); ++i) {
            glyph.getContourEnds().add(file.getUint16());
        }

        file.seek(file.getUint16() + file.getPosition());

        if (glyph.getNumberOfContours() == 0) {
            return;
        }

        int numPoints = Collections.max(glyph.getContourEnds()) + 1;

        short[] flags = new short[numPoints];
        boolean[] isCurves = new boolean[numPoints];

        for (int i = 0; i < numPoints; ++i) {
            short flag = file.getUint8();
            flags[i] = flag;
            isCurves[i] = (flag & ON_CURVE) > 0;

            if ((flag & REPEAT) != 0) {
                short repeatCount = file.getUint8();
                int j = i + 1;
                i += repeatCount;
                while (repeatCount-- != 0) {
                    flags[j] = flag;
                    isCurves[j] = (flag & ON_CURVE) > 0;
                    ++j;
                }
            }
        }

        int[] xs = new int[numPoints];
        int[] ys = new int[numPoints];
        readPoints(xs, flags, X_DELTA, X_IS_BYTE);
        readPoints(ys, flags, Y_DELTA, Y_IS_BYTE);

        for (int i = 0; i < numPoints; ++i) {
            glyph.getPoints().add(new GlyphPoint(
                    xs[i],
                    ys[i],
                    isCurves[i]
            ));
        }
    }

    private void readPoints(
            int[] points, short[] flags, int deltaFlag, int isByteFlag
    ) {
        int value = 0;
        for (int i = 0; i < points.length; ++i) {
            short flag = flags[i];
            if ((flag & isByteFlag) != 0) {
                if ((flag & deltaFlag) != 0) {
                    value += file.getUint8();
                } else {
                    value -= file.getUint8();
                }
            } else if (((-flag - 1) & deltaFlag) != 0) {
                value += file.getInt16();
            }
            points[i] = value;
        }
    }

    private int readCompoundGlyph() {
        int index = -1;
        int flags = MORE_COMPONENTS;
        while ((flags & MORE_COMPONENTS) != 0) {
            flags = file.getUint16();
            int i = file.getUint16();  // glyph index
            if (index == -1) {
                index = i;
            }

            if ((flags & ARG_1_AND_2_ARE_WORDS) != 0) {
                file.getInt16();  // dest point index
                file.getInt16();  // src point index
            } else {
                file.getUint8();
                file.getUint8();
            }

            if ((flags & WE_HAVE_A_SCALE) != 0) {
                file.get2Dot14();  // matrix.a
            } else if ((flags & WE_HAVE_AN_X_AND_Y_SCALE) != 0) {
                file.get2Dot14();  // matrix.a
                file.get2Dot14();  // matrix.d
            } else if ((flags & WE_HAVE_A_TWO_BY_TWO) != 0) {
                file.get2Dot14();  // matrix.a
                file.get2Dot14();  // matrix.b
                file.get2Dot14();  // matrix.c
                file.get2Dot14();  // matrix.d
            }
        }

        if ((flags & WE_HAVE_INSTRUCTIONS) != 0) {
            file.seek(file.getUint16() + file.getPosition());
        }
        return index;
    }

    public Glyph getGlyph(char charCode) {
        if (charCode == ' ') {
            return SPACE;
        }
        Glyph glyph = readGlyph(cmap.getIndex(charCode));
        return glyph == null ? UNKNOWN : glyph;
    }
}
