package clientserver;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileLoaded {
    private int hash;
    private long sizeCounter;
    private String name;
    private long size;
    private Path file;
    private Map<Integer, FilePartLoaded> parts;

    public FileLoaded(int hash, String name, long size) {
        this.hash = hash;
        this.name = name;
        this.size = size;
        this.file = Path.of(name);
        this.parts = new HashMap<>();
        sizeCounter = 0;
    }

    public void addPart(int num, int length) {
        long startIndex = 0;
        if (parts.containsKey(num-1)) {
            FilePartLoaded part = parts.get(num - 1);
            startIndex = part.getStartIndex()+part.getLength();
        }
        this.parts.put(num, new FilePartLoaded(num, startIndex, length));
        sizeCounter += length;
    }

    public String getName() {
        return name;
    }

    public long getSizeCounter() {
        return sizeCounter;
    }

    public void UploadFile() {

    }
}
