package ru.daniilazarnov.test;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;

import java.io.*;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Commands {

    private int nextLength;
    private long fileLength;
    private long receivedFileLength;
    private BufferedOutputStream out;
    private TransferState transferState = TransferState.READY;

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
        buf.writeByte((byte) 45);
        buf.writeBytes(Utils.convertListToByteBuf(list));
        ChannelFuture transferOperationFuture = ctx.writeAndFlush(buf);
        if (finishListener != null) {
            transferOperationFuture.addListener(finishListener);
        }
    }

    public void removeFile(String path) throws IOException {
        try {
            Files.deleteIfExists(Paths.get(path));
        } catch(NoSuchFileException e){
            System.out.println("No such file/directory exists");
        } catch(DirectoryNotEmptyException e){
            System.out.println("Directory is not empty.");
        } catch(IOException e){
            System.out.println("Invalid permissions.");
        }

        System.out.println("Deletion successful.");
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
}
