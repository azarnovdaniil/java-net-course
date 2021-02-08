package ru.gb.putilin.cloudstorage.server.handlers.upload;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import ru.gb.putilin.cloudstorage.server.handlers.FileHandler;

import java.io.*;

public class UploadHandler implements FileHandler {

    private ChannelHandlerContext ctx;
    private ByteBuf buf;
    private boolean isComplete = false;
    private UploadState currentState = UploadState.NAME_LENGTH;
    private String fileName;

    private int nextLength;
    private long fileLength;
    private long receivedFileLength;
    private BufferedOutputStream out;


    public UploadHandler(ChannelHandlerContext ctx) {
        this.ctx = ctx;
    }

    @Override
    public void handle() {
        if (currentState == UploadState.NAME_LENGTH) {
            if (buf.readableBytes() >= 4) {
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
                    fileName = new String(fileNameBytes, "UTF-8");
                    System.out.println("STATE: Filename received:" + fileName);

                    out = new BufferedOutputStream(
                            new FileOutputStream("C:/Temp/" + fileName)
                    );
                    currentState = UploadState.FILE_LENGTH;
                } catch (FileNotFoundException | UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }

        if (currentState == UploadState.FILE_LENGTH) {
            if (buf.readableBytes() >= 8) {
                fileLength = buf.readLong();
                System.out.println("STATE: File length received - " + fileLength);
                currentState = UploadState.FILE;
            }
        }

        if (currentState == UploadState.FILE) {
            while (buf.readableBytes() > 0) {
                try {
                    out.write(buf.readByte());
                    receivedFileLength++;
                    if (fileLength == receivedFileLength) {
                        isComplete = true;
                        System.out.println("File received");
                        out.close();
                        break;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
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
