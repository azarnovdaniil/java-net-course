package clientserver;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class FileLoaded {
    private static final int SIZE_TO_WRITE = 1024 * 1024 * 100;
    private final int hash;
    private long sizeCounter;
    private final String name;
    private final long size;
    private final Path file;
    private final Map<Integer, FilePartLoaded> parts;
    private BufferedOutputStream bufferedOutputStream;

    public FileLoaded(int hash, String name, long size, Path path) {
        this.hash = hash;
        this.name = name;
        this.size = size;
        this.file = path;
        this.parts = new HashMap<>();
        sizeCounter = 0;

        try {
            Files.deleteIfExists(file);
            bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file.toFile(), true), SIZE_TO_WRITE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addPart(int num, int length, byte[] content) throws IOException {
        long startIndex = 0;
        if (parts.containsKey(num - 1)) {
            FilePartLoaded part = parts.get(num - 1);
            startIndex = part.getStartIndex() + part.getLength();
        }
        this.parts.put(num, new FilePartLoaded(num, startIndex, length));
        sizeCounter += length;

        bufferedOutputStream.write(content);

        if (size == sizeCounter) {
            bufferedOutputStream.flush();
            bufferedOutputStream.close();
        }
    }

    public long getSizeCounter() {
        return sizeCounter;
    }
}
