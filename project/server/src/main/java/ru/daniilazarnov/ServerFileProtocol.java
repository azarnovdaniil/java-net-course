package ru.daniilazarnov;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;

import static io.netty.buffer.Unpooled.buffer;

public class ServerFileProtocol {

    private String serverPath = "project/server/";
    private static final int BUF_SIZE = 1024;
    private ChannelHandlerContext ctx;

    public String getServerPath() {
        return serverPath;
    }

    public void setCtx(ChannelHandlerContext ctx) {
        this.ctx = ctx;
    }

    public void writeFile(String fileName, Path path) {
        try {
            if (Files.exists(path) && !Files.isDirectory(path)) {
                sendCommand(Commands.UPLOAD, fileName);
                try (InputStream inputStream = Files.newInputStream(path)) {
                    byte[] data = new byte[BUF_SIZE];
                    while (inputStream.available() >= BUF_SIZE) {
                        inputStream.read(data);
                        sendData(data);
                    }
                    byte[] dataEnd = new byte[inputStream.available()];
                    while (inputStream.available() > 0) {
                        inputStream.read(dataEnd);
                        sendData(dataEnd);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void createFile(Path path) {
        try {
            Files.deleteIfExists(path);
            Files.createFile(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendFilesList() {
        Path path = Paths.get(serverPath);
        System.out.println(path.toString());
        ArrayList<String> filesList = new ArrayList<>();
        StringBuilder result = new StringBuilder();
        try {
            Files.walkFileTree(path, new SimpleFileVisitor<>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    filesList.add(file.getFileName().toString());
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                    if (dir.compareTo(path) != 0) {
                        filesList.add(dir.getFileName().toString());
                        return FileVisitResult.SKIP_SUBTREE;
                    }
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (String o : filesList) {
            result.append(o).append(" ");
        }
        System.out.println(result.toString());
    }

    private void sendData(Object data) {
        ByteBuf buf = buffer(BUF_SIZE);
        buf.writeBytes((byte[]) data);
        ctx.channel().writeAndFlush(buf);
    }

    private void sendCommand(Commands command, String commandInfo) {
        ByteBuf buf = buffer(BUF_SIZE);
        try {
            buf.writeByte(command.getCommBytes());
            buf.writeInt(commandInfo.getBytes().length);
            buf.writeBytes(commandInfo.getBytes());
            if (command == Commands.UPLOAD) {
                buf.writeLong(Files.size(Paths.get(serverPath + commandInfo)));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        ctx.channel().writeAndFlush(buf);
    }

    public void createDir(Path path) {
        if (!Files.exists(path)) {
            try {
                Files.createDirectory(path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void moveForward(String fileName) {
        if (Files.isDirectory(Paths.get(serverPath + fileName))) {
            serverPath += fileName;
        }
    }

    public void moveBack() {
        String[] currentPathArr = serverPath.split(File.separator);
        if (currentPathArr.length >= 2) {
            StringBuilder previousPath = new StringBuilder();
            for (int i = 0; i <= currentPathArr.length - 2; i++) {
                previousPath.append(currentPathArr[i]).append(File.separator);
            }
            serverPath = previousPath.toString();
            sendFilesList();
        }
    }

    public void deleteFile(Path path) {
        try {
            if (!Files.isDirectory(path)) {
                Files.deleteIfExists(path);
            } else {
                Files.walkFileTree(path, new SimpleFileVisitor<>() {
                    @Override
                    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                        Files.delete(file);
                        return FileVisitResult.CONTINUE;
                    }

                    @Override
                    public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                        Files.delete(dir);
                        return FileVisitResult.CONTINUE;
                    }
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
