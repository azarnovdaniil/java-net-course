package clientserver;

public class FilePartLoaded {
    private int num;
    private long startIndex;
    private int length;

    public FilePartLoaded(int num, long startIndex, int length) {
        this.num = num;
        this.startIndex = startIndex;
        this.length = length;
    }

    public int getNum() {
        return num;
    }

    public long getStartIndex() {
        return startIndex;
    }

    public int getLength() {
        return length;
    }
}
