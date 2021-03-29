package ru.kgogolev;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.Unpooled;
import io.netty.channel.DefaultFileRegion;
import io.netty.channel.FileRegion;

import java.io.IOException;
import java.nio.channels.Channel;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Date;

public class FileSystem {
    public static final byte MAGIC_BYTE = (byte) 25;

    public void walkFileTree(String path) throws IOException {
        Path currentPath  = Paths.get(path);
        Files.walkFileTree(currentPath, new SimpleFileVisitor<Path>() {

            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                if (!dir.toFile().canRead()||dir.toFile().isHidden()){
                    return FileVisitResult.CONTINUE;
                } else if (dir.getNameCount()==currentPath.getNameCount()+1) {
                    System.out.println(String.format("subdir: %48s |",
                            dir.getName(dir.getNameCount() - 1)));
                }
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                try {
                    if (!file.toFile().canRead() || file.toFile().isHidden()) {
                        return FileVisitResult.CONTINUE;
                    } else if (file.getNameCount() <= currentPath.getNameCount() + 1) {
                        System.out.println(String.format("file: %50s | %20s | %10d",
                                file.toFile().getName(),
                                new Date(file.toFile().lastModified()),
                                attrs.size()));
                    }
                } catch (Exception e){
                    return FileVisitResult.CONTINUE;
                }
                return FileVisitResult.CONTINUE;
            }
        });
    }
    public void walkAllFileTree(String path) throws IOException {
        Path currentPath  = Paths.get(path);
        Files.walkFileTree(currentPath, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                System.out.println(dir);

                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                System.out.println(file.toFile().getName());
                return FileVisitResult.CONTINUE;
            }
        });
    }
//    public void run(Path path) throws IOException {
//        sendFile(path, future ->  {
//
//            if (!future.isSuccess()) {
//                future.cause().printStackTrace();
//                network.stop();
//            }
//            if (future.isSuccess()) {
//                System.out.println("Файл успешно передан");
//                network.stop();
//            }
//        });
//    }

    public ByteBuf sendFile(Path path) throws IOException {

        FileRegion region = new DefaultFileRegion(path.toFile(), 0, Files.size(path));

        ByteBuf buf = Unpooled.buffer();
//        buf = ByteBufAllocator.DEFAULT.directBuffer(1);
        buf.writeByte(MAGIC_BYTE);
//        network.getCurrentChannel().writeAndFlush(buf);

        byte[] filenameBytes = path.getFileName().toString().getBytes(StandardCharsets.UTF_8);
//        buf = ByteBufAllocator.DEFAULT.directBuffer(Integer.SIZE / Byte.SIZE);
        buf.writeInt(filenameBytes.length);
//        buf.alloc().
//        channel.writeAndFlush(buf);

//        buf = ByteBufAllocator.DEFAULT.directBuffer(filenameBytes.length);
        buf.writeBytes(filenameBytes);
//        channel.writeAndFlush(buf);

//        buf = ByteBufAllocator.DEFAULT.directBuffer(Long.SIZE / Byte.SIZE);
        buf.writeLong(Files.size(path));
//        channel.writeAndFlush(buf);
        buf.writeByte(101);
        buf.writeByte(101);
        buf.writeByte(120);
//        while (buf.readableBytes()>0){
//            System.out.print(buf.readByte() +" ");
//        }
        return buf;
//        System.out.println(buf.toString());
//        ChannelFuture transferOperationFuture = channel.writeAndFlush(region);
//        if (finishListener != null) {
//            transferOperationFuture.addListener(finishListener);
        }
    }



