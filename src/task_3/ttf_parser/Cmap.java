package task_3.ttf_parser;

import java.util.*;


class CMap {
    private static class Segment {
        final int idRangeOffset;
        final int startCode;
        final int endCode;
        final int idDelta;

        Segment(int idRangeOffset, int startCode, int endCode, int idDelta) {
            this.idRangeOffset = idRangeOffset;
            this.startCode = startCode;
            this.endCode = endCode;
            this.idDelta = idDelta;
        }
    }

    private final Map<Character, Integer> charMap = new HashMap<>();

    public CMap(BinaryReader file) {
        List<Segment> segments = readSegments(file);
        for (char charCode = 0; charCode < Character.MAX_VALUE; ++charCode) {
            charMap.put(
                    charCode,
                    readIndex(charCode, file, segments)
            );
        }
    }

    private List<Segment> readSegments(BinaryReader file) {
        List<Segment> segments = new ArrayList<>();

        file.getUint16();  // format
        file.getUint16();  // length
        file.getUint16();  // language

        int segCount = file.getUint16() / 2;

        file.getUint16();  // searchRange
        file.getUint16();  // entrySelector
        file.getUint16();  // rangeShift

        int[] endCodes = new int[segCount];
        for (int i = 0; i < segCount; i++) {
            endCodes[i] = file.getUint16();
        }

        file.getUint16();  // reservePAd

        int[] startCodes = new int[segCount];
        for (int i = 0; i < segCount; i++) {
            startCodes[i] = file.getUint16();
        }

        int[] idDeltas = new int[segCount];
        for (int i = 0; i < segCount; i++) {
            idDeltas[i] = file.getUint16();
        }

        int[] idRangeOffsets = new int[segCount];
        for (int i = 0; i < segCount; i++) {
            int ro = file.getUint16();
            if (ro != 0) {
                idRangeOffsets[i] = (int) (file.getPosition() - 2 + ro);
            } else {
                idRangeOffsets[i] = 0;
            }
        }

        for (int i = 0; i < segCount; ++i) {
            segments.add(
                    i,
                    new Segment(
                            idRangeOffsets[i],
                            startCodes[i],
                            endCodes[i],
                            idDeltas[i]
                    )
            );
        }
        return segments;
    }

    private int readIndex(char charCode, BinaryReader file, List<Segment> segments) {
        int index = -1;

        for (Segment segment : segments) {
            if (segment.startCode <= charCode && segment.endCode >= charCode) {
                int glyphIndexAddress;
                if (segment.idRangeOffset != 0) {
                    glyphIndexAddress = segment.idRangeOffset + 2 *
                            (charCode - segment.startCode);
                    file.seek(glyphIndexAddress);
                    index = file.getUint16();
                } else {
                    index = (segment.idDelta + charCode) & 0xffff;
                }
                break;
            }
        }

        return index;
    }

    public int getIndex(char charCode) {
        return charMap.get(charCode);
    }
}
