package task_3.ttf_parser;


class HeadTable {
    private final long checksum;
    private final long offset;
    private final long length;

    public HeadTable(long checksum, long offset, long length) {
        this.checksum = checksum;
        this.offset = offset;
        this.length = length;
    }

    public long getChecksum() {
        return checksum;
    }

    public long getOffset() {
        return offset;
    }

    public long getLength() {
        return length;
    }

    @Override
    public String toString() {
        return "HeadTable{" +
                "checksum=" + checksum +
                ", offset=" + offset +
                ", length=" + length +
                '}';
    }
}
