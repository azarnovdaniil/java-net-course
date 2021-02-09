package clientserver;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileLoaded {
    private int hash;
    private String name;
    private long size;
    private File file;
    private List<FilePartLoaded> parts;

    public FileLoaded(int hash, String name, long size) {
        this.hash = hash;
        this.name = name;
        this.size = size;
        this.file = new File(name);
        this.parts = new ArrayList<>();
    }

    public void addPart(int num, long startIndex, int length) {
        this.parts.add(new FilePartLoaded(num, startIndex, length));
    }

}
