package ru.daniilazarnov.server.operations.download;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import ru.daniilazarnov.common.files.FileSender;
import ru.daniilazarnov.common.handlers.Handler;
import ru.daniilazarnov.common.FilePackageConstants;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;

public class DownloadHandler implements Handler {

    private DownloadHandlerState state = DownloadHandlerState.PATH_LENGTH;

    private Channel channel;
    private ByteBuf buf;
    private String root;

    private int pathLength;
    private String pathString;

    public DownloadHandler(Channel channel, String root) {
        this.channel = channel;
        this.root = root;
    }

    @Override
    public void handle() throws IOException {

        if (state == DownloadHandlerState.PATH_LENGTH) {
            if (buf.readableBytes() >= FilePackageConstants.NAME_LENGTH_BYTES.getCode()) {
                pathLength = buf.readInt();
                state = DownloadHandlerState.PATH;
            }
        }

        if (state == DownloadHandlerState.PATH) {
            if (buf.readableBytes() == pathLength) {
                byte[] pathBytes = new byte[pathLength];
                buf.readBytes(pathBytes);
                pathString = new String(pathBytes, StandardCharsets.UTF_8);
                state = DownloadHandlerState.SEND_FILE;
            }
        }

        if (state == DownloadHandlerState.SEND_FILE) {
            channel.flush();
            FileSender fileSender = new FileSender(channel);
            fileSender.sendFile(Paths.get(pathString),
                    future -> {
                        if (!future.isSuccess()) {
                            future.cause().printStackTrace();
                        }
                        if (future.isSuccess()) {
                            System.out.println("File has been sent to the client");
                            state = DownloadHandlerState.COMPLETE;
                        }
                    });
        }
    }

    @Override
    public boolean isComplete() {
        return state == DownloadHandlerState.COMPLETE;
    }

    @Override
    public void setBuffer(ByteBuf buf) {
        this.buf = buf;
    }
}
