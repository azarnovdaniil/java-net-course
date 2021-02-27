package ru.daniilazarnov.actual;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.ArrayList;

public class Commands {

    private int nextLength;
    private long fileLength;
    private long receivedFileLength;
    private BufferedOutputStream out;
    private TransferState transferState = TransferState.READY;

    public void help() {
        String path = new File("").getAbsolutePath();
        try(FileReader reader = new FileReader(path + "\\help.txt")){
            int ch;
            while((ch = reader.read()) != -1){
                System.out.print((char)ch);
            }
        }
        catch(IOException e){
            System.out.println(e.getMessage());
        }
    }

    public void sendMessage(String msg, ChannelHandlerContext ctx){
        ByteBuf buf = ByteBufAllocator.DEFAULT.buffer();
        buf.writeByte(Signals.TEXT.get());
        buf.writeBytes(Utils.convertToByteBuf(msg));
        ctx.writeAndFlush(buf);
    }

    public void listFiles(String directory, ChannelHandlerContext ctx, ChannelFutureListener finishListener) throws IOException {
        File dir = new File(directory);
        File[] files = dir.listFiles();
        ArrayList<String> list = new ArrayList<>();
        if (files != null && files.length > 0) {
            for (File file : files) {
                if (!file.isDirectory()) {
                    list.add(String.format("[%s] size: %s", file.getName(), Utils.bytesConverter(file.length())));
                }
            }
        }
        if (list.size() == 0){
            list.add("Files not found");
        }
        ByteBuf buf = ByteBufAllocator.DEFAULT.buffer();
        buf.writeByte(Signals.LS.get());
        buf.writeBytes(Utils.convertListToByteBuf(list));
        ChannelFuture transferOperationFuture = ctx.writeAndFlush(buf);
        if (finishListener != null) {
            transferOperationFuture.addListener(finishListener);
        }
    }

    public boolean removeFile(String path) {
        try {
            if (Files.deleteIfExists(Paths.get(path))){
                System.out.printf("File [%s] was deleted", path);
                return true;
            }
        } catch(NoSuchFileException e){
            System.out.println("No such file/directory exists");
        } catch(DirectoryNotEmptyException e){
            System.out.println("Directory is not empty.");
        } catch(IOException e){
            System.out.println("Invalid permissions.");
        }
        return false;
    }

    public boolean receiveFile(ByteBuf buf, String dir) throws IOException {
        while (buf.readableBytes() > 0) {
            try {
                if (transferState == TransferState.READY) {
                    if (buf.readByte() == Signals.UPLOAD.get()) {
                        transferState = TransferState.NAME_LENGTH;
                        receivedFileLength = 0L;
                        System.out.println("STATE: Start file receiving");
                    } else {
                        System.out.println("ERROR: Invalid first byte - ");
                    }
                }

                if (transferState == TransferState.NAME_LENGTH) {
                    if (buf.readableBytes() >= (Integer.SIZE / Byte.SIZE)) {
                        System.out.println("STATE: Get filename length");
                        nextLength = buf.readInt();
                        transferState = TransferState.NAME;
                    }
                }
                if (transferState == TransferState.NAME) {
                    if (buf.readableBytes() >= nextLength) {
                        byte[] fileName = new byte[nextLength];
                        buf.readBytes(fileName);
                        System.out.println("STATE: Filename received - _" + new String(fileName, "UTF-8"));
                        out = new BufferedOutputStream(new FileOutputStream(dir + new String(fileName)));
                        transferState = TransferState.FILE_LENGTH;
                    }
                }

                if (transferState == TransferState.FILE_LENGTH) {
                    if (buf.readableBytes() >= (Long.SIZE / Byte.SIZE)) {
                        fileLength = buf.readLong();
                        System.out.println("STATE: File length received - " + fileLength);
                        transferState = TransferState.FILE;
                    }
                }

                if (transferState == TransferState.FILE) {
                    while (buf.readableBytes() > 0) {
                        out.write(buf.readByte());
                        receivedFileLength++;
                        if (fileLength == receivedFileLength) {
                            transferState = TransferState.READY;
                            System.out.println("File received");
                            out.close();
                            return true;
                        }
                    }
                }
            } finally {
                if (buf.readableBytes() == 0) {
                    buf.release();
                }
            }
        }
        return false;
    }

    public void sendFile(Path path, Channel channel, ChannelFutureListener finishListener) throws IOException {

        FileRegion region = new DefaultFileRegion(path.toFile(), 0, Files.size(path));

        ByteBuf buf;

        buf = ByteBufAllocator.DEFAULT.directBuffer(1);
        buf.writeByte(Signals.START_UPLOAD.get());
        channel.writeAndFlush(buf);

        buf = ByteBufAllocator.DEFAULT.directBuffer(1);
        buf.writeByte(Signals.UPLOAD.get());
        channel.writeAndFlush(buf);

        byte[] filenameBytes = path.getFileName().toString().getBytes(StandardCharsets.UTF_8);
        buf = ByteBufAllocator.DEFAULT.directBuffer(Integer.SIZE / Byte.SIZE);
        buf.writeInt(filenameBytes.length);
        channel.writeAndFlush(buf);

        buf = ByteBufAllocator.DEFAULT.directBuffer(filenameBytes.length);
        buf.writeBytes(filenameBytes);
        channel.writeAndFlush(buf);

        buf = ByteBufAllocator.DEFAULT.directBuffer(Long.SIZE / Byte.SIZE);
        buf.writeLong(Files.size(path));
        channel.writeAndFlush(buf);

        ChannelFuture transferOperationFuture = channel.writeAndFlush(region);
        if (finishListener != null) {
            transferOperationFuture.addListener(finishListener);
        }
    }
}
