package clientserver;

import java.io.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileLoaded {
    private static final int SIZE_TO_WRITE = 1024*1024*100;
    private int hash;
    private long sizeCounter;
    private String name;
    private long size;
    private Path file;
    private Map<Integer, FilePartLoaded> parts;

    public FileLoaded(int hash, String name, long size, Path path) {
        this.hash = hash;
        this.name = name;
        this.size = size;
        this.file = path;
        this.parts = new HashMap<>();
        sizeCounter = 0;
    }

    public void addPart(int num, int length, byte[] content) {
        long startIndex = 0;
        if (parts.containsKey(num-1)) {
            FilePartLoaded part = parts.get(num - 1);
            startIndex = part.getStartIndex()+part.getLength();
        }
        this.parts.put(num, new FilePartLoaded(num, startIndex, length, content));
        sizeCounter += length;

        // проверить не пора ли писать в файл
        if ((size==sizeCounter) || (sizeCounter>SIZE_TO_WRITE)) {
            try {
                writeToFile(0, 1);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void writeToFile(int start, int end) throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(file.toFile(), true);
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream, 1024*1024*100);
        for (int i = start; i < end; i++) {
            bufferedOutputStream.write(parts.get(i).getContent());
            parts.get(i).clear();
        }
        bufferedOutputStream.flush();
        fileOutputStream.close();
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
