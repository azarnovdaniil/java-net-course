package ru.atoroschin;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

public class FileWorker {
    private static final int MEGA_BYTE = 1024 * 1024;
    private static final int PART_SIZE = 10 * MEGA_BYTE;
    private Path currentPath;
    private Path basePath;
    private String serverPath;
    private long maxVolume;

    public FileWorker(String currentDir, String baseDir, long maxVolume) throws IOException {
        checkDir(Path.of(currentDir));
        currentPath = Path.of(currentDir).toRealPath();

        this.basePath = Path.of(baseDir);
        this.serverPath = "";
        if (maxVolume == 0) {
            this.maxVolume = (long) MEGA_BYTE * MEGA_BYTE;
        } else {
            this.maxVolume = maxVolume * MEGA_BYTE;
        }
    }

    public String getServerPathOnServer() {
        Path pathRelative = basePath.toAbsolutePath().relativize(currentPath);
        return pathRelative.toString();
    }

    public String getServerPathOnClient() {
        return serverPath;
    }

    public void setServerPath(String serverPath) {
        this.serverPath = serverPath;
    }

    public String getCurrentDir() {
        return currentPath.toAbsolutePath().toString();
    }

    public void sendFile(ChannelHandlerContext ctx, String content, byte signal, byte[] bytes) {
        try {
            Path file = currentPath.resolve(content);
            if (Files.exists(file)) {
                long volume = Files.size(file);
                long sendVolume = 0;
                int countPar = (int) volume / PART_SIZE + 1;

                final int countFour = 4;
                int lenServiceData = 1 + countFour + countFour + bytes.length;
                int lengthReadData = PART_SIZE - lenServiceData;
                byte[] buffer = new byte[lengthReadData];

                int partNum = countPar == 1 ? -1 : 1;
                int i = 0;
                FileInputStream fileInputStream = new FileInputStream(file.toFile());
                BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream, lengthReadData);
                while (sendVolume < volume) {
                    int readLength = bufferedInputStream.read(buffer);
                    ByteBuf bufOut = ByteBufAllocator.DEFAULT.buffer(PART_SIZE);
                    bufOut.writeByte(signal); // команда
                    bufOut.writeInt(lenServiceData + readLength); // длина сообщения
                    bufOut.writeInt(partNum + i); // номер части
                    bufOut.writeBytes(bytes); // служебные данные. все, кроме содержимого файла
                    bufOut.writeBytes(buffer, 0, readLength); // содержимое файла

                    ctx.writeAndFlush(bufOut);
                    sendVolume += readLength;
                    i++;
                }
                bufferedInputStream.close();
                fileInputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public byte[] makePrefixForFileSend(String fileName) {
        File file = currentPath.resolve(fileName).toFile();
        if (!file.exists()) {
            return new byte[]{(byte) 0};
        }
        String name = file.getName();
        int hash = getHash(file);
        int lengthResponse;
        long volume = file.length();
        final int countFour = 4;
        final int countEight = 8;
        lengthResponse = countFour + countFour + name.getBytes(StandardCharsets.UTF_8).length + countEight;
        ByteBuf buf = ByteBufAllocator.DEFAULT.buffer(lengthResponse);

        buf.writeInt(hash);
        buf.writeInt(name.getBytes(StandardCharsets.UTF_8).length);
        buf.writeBytes(name.getBytes(StandardCharsets.UTF_8));
        buf.writeLong(volume);

        byte[] bufOut = new byte[lengthResponse];
        buf.readBytes(bufOut, 0, lengthResponse);
        return bufOut;
    }

    private int getHash(File file) {
        return file.hashCode();
    }

    public String receiveFile(ByteBuf buf, Map<Integer, FileLoaded> uploadedFiles) throws IOException {
        buf.readInt(); // длина всего сообщения
        int partNum = buf.readInt();
        int hash = buf.readInt();
        int fileNameSize = buf.readInt();
        ByteBuf fileNameBuf = buf.readBytes(fileNameSize);
        String fileName = fileNameBuf.toString(StandardCharsets.UTF_8);
        fileNameBuf.release();
        long fileSize = buf.readLong();
        FileLoaded fileLoaded;
        Path path = currentPath.resolve(fileName);
        long busyVolume = getVolumeDir(basePath);
        if (busyVolume + fileSize <= maxVolume) {
            if (uploadedFiles.containsKey(hash)) {
                fileLoaded = uploadedFiles.get(hash);
            } else {
                fileLoaded = new FileLoaded(hash, fileName, fileSize, path);
                uploadedFiles.put(hash, fileLoaded);
            }
            int writeSize = buf.readableBytes();
            byte[] bufIn = new byte[writeSize];

            buf.readBytes(bufIn);
            fileLoaded.addPart(partNum, writeSize, bufIn);

            if (fileLoaded.getSizeCounter() == fileSize) {
                uploadedFiles.remove(hash);
                return fileName;
            }
        }
        throw new IOException();
    }

    private long getVolumeDir(Path path) {
        try {
            return Files.walk(path)
                    .map(Path::toFile)
                    .map(File::length)
                    .reduce(Long::sum).get();
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public long getFreeSpace() {
        return maxVolume - getVolumeDir(basePath);
    }

    public List<String> getFileListInDir() throws IOException {
        return Files.list(Path.of(getCurrentDir()))
                .map(Path::toFile)
                .map(File::getName)
                .collect(toList());
    }

    public void changeCurrentDir(String newDir) {
        if (Files.exists(currentPath.resolve(newDir))) {
            try {
                if (basePath.toFile().exists()) {
                    if (currentPath.resolve(newDir).toRealPath().startsWith(basePath.toRealPath())) {
                        currentPath = currentPath.resolve(newDir).toRealPath();
                    }
                } else {
                    currentPath = currentPath.resolve(newDir).toRealPath();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendCommandWithStringList(ChannelHandlerContext ctx, List<String> list, byte signal) {
        ByteBuf bufOut = BufWorker.makeBufFromList(list, signal);
        ctx.writeAndFlush(bufOut);
    }

    public void makeDir(String nameDir) {
        if (!Files.exists(currentPath.resolve(nameDir))) {
            try {
                Files.createDirectory(currentPath.resolve(nameDir));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void checkDir(Path path) throws IOException {
        if (!Files.exists(path)) {
            Files.createDirectory(path);
        }
    }

    public void removeDir(String nameDir) {
        try {
            Files.deleteIfExists(currentPath.resolve(nameDir));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void moveFile(String fileName, String dirName) {
        try {
            Path pathFile = currentPath.resolve(fileName);
            Path pathDir = currentPath.resolve(dirName);
            if (Files.exists(pathFile) && Files.exists(pathDir)
                    && Files.isRegularFile(pathFile) && Files.isDirectory(pathDir)) {
                Files.move(pathFile, pathDir.resolve(fileName));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void renameFile(String fileName, String newFileName) {
        try {
            Path pathFile = currentPath.resolve(fileName);
            Path pathNewFile = currentPath.resolve(newFileName);
            if (Files.exists(pathFile) && !Files.exists(pathNewFile)) {
                Files.move(pathFile, pathNewFile);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void appendBasePath(String userDir) throws IOException {
        basePath = basePath.resolve(userDir);
        currentPath = currentPath.resolve(userDir);
        checkDir(basePath);
    }

    public void setMaxVolume(int maxVolume) {
        this.maxVolume = (long) MEGA_BYTE * maxVolume;
    }
}
