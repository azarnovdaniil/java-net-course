package ru.daniilazarnov.server.handlers.upload;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import ru.daniilazarnov.common.handlers.Handler;
import ru.daniilazarnov.common.FilePackageConstants;

import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.EnumSet;

public class UploadHandler implements Handler {

    private final ChannelHandlerContext ctx;
    private ByteBuf buf;
    private boolean isComplete = false;
    private UploadState currentState = UploadState.NAME_LENGTH;
    private String fileName;

    private int nextLength;
    private long fileLength;
    private long receivedFileLength;
    private FileChannel fileChannel;
    private String root;


    public UploadHandler(ChannelHandlerContext ctx, String root) {
        this.ctx = ctx;
        this.root = root;
    }

    @Override
    public void handle() throws IOException {
        if (currentState == UploadState.NAME_LENGTH) {
            if (buf.readableBytes() >= FilePackageConstants.NAME_LENGTH_BYTES.getCode()) {
                System.out.println("STATE: Get filename length");
                nextLength = buf.readInt();
                currentState = UploadState.NAME;
            }
        }

        if (currentState == UploadState.NAME) {
            if (buf.readableBytes() >= nextLength) {
                try {
                    byte[] fileNameBytes = new byte[nextLength];
                    buf.readBytes(fileNameBytes);
                    fileName = new String(fileNameBytes, StandardCharsets.UTF_8);
                    System.out.println("STATE: Filename received:" + fileName);

                    fileChannel = FileChannel.open(Path.of(root + fileName),
                            EnumSet.of(StandardOpenOption.CREATE,
                                    StandardOpenOption.TRUNCATE_EXISTING,
                                    StandardOpenOption.WRITE));

                    currentState = UploadState.FILE_LENGTH;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        if (currentState == UploadState.FILE_LENGTH) {
            if (buf.readableBytes() >= FilePackageConstants.FILE_LENGTH_BYTES.getCode()) {
                fileLength = buf.readLong();
                System.out.println("STATE: File length received - " + fileLength);
                currentState = UploadState.FILE;
            }
        }

        if (currentState == UploadState.FILE) {
            try {
                receivedFileLength += buf.readBytes(fileChannel, receivedFileLength, buf.readableBytes());
                System.out.println("Part is received");
                if (fileLength == receivedFileLength) {
                    isComplete = true;
                    System.out.println("File received");
                    fileChannel.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean isComplete() {
        return isComplete;
    }

    @Override
    public void setBuffer(ByteBuf buf) {
        this.buf = buf;
    }
}
