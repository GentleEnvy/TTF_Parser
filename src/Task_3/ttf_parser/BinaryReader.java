package task_3.ttf_parser;

import java.io.*;


class BinaryReader {
    private long position = 0;
    private final File file;
    private InputStream inputStream;

    public BinaryReader(File file) {
        this.file = file;
        try {
            inputStream = new BufferedInputStream(
                    new FileInputStream(file),
                    (int) file.length()
            );
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public long seek(long position) {
        long oldPosition = this.position;
        this.position = position;

        try {
            //inputStream.skip(position - oldPosition);
            inputStream = new BufferedInputStream(
                    new FileInputStream(file),
                    (int) file.length()
            );
            inputStream.skip(position);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }

        return oldPosition;
    }

    public long getPosition() {
        return position;
    }

    public short getUint8() {
        try {
            short byte_ = (short) inputStream.read();
            ++position;
            return byte_;
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public int getUint16() {
        return (int) (((getUint8() << 8) | getUint8()) & 0xffffffffL);
    }

    public int getInt32() {
        return (
                (getUint8() << 24) |
                        (getUint8() << 16) |
                        (getUint8() << 8) |
                        getUint8()
        );
    }

    public long getUint32() {
        return getInt32() & 0xffffffffL;
    }

    public short getInt16() {
        int result = getUint16();
        if ((result & 0x8000) != 0) {
            result -= (1 << 16);
        }
        return (short) result;
    }

    public short getFWord() {
        return getInt16();
    }

    public float get2Dot14() {
        return getInt16() / (float) (1 << 14);
    }

    public double getFixed() {
        return getInt32() / (double) (1 << 16);
    }

    public String getString(int length) {
        StringBuilder string = new StringBuilder();
        for (int i = 0; i < length; ++i) {
            string.append((char) getUint8());
        }
        return string.toString();
    }

    public long getDate() {
        return getUint32() * 0x100000000L + getUint32();
    }
}
