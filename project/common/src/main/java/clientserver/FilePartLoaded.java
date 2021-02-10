package clientserver;

public class FilePartLoaded {
    private int num;
    private long startIndex;
    private int length;


    private byte[] content;

    public FilePartLoaded(int num, long startIndex, int length, byte[] content) {
        this.num = num;
        this.startIndex = startIndex;
        this.length = length;
        this.content = content;
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

    public byte[] getContent() {
        return content;
    }

    public void clear() {
        content = null;
    }
}
