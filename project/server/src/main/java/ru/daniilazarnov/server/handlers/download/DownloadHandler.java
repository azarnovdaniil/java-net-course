package ru.daniilazarnov.server.handlers.download;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import ru.daniilazarnov.common.handlers.Handler;
import ru.daniilazarnov.common.FilePackageConstants;

import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

public class DownloadHandler implements Handler {

    private DownloadHandlerState state = DownloadHandlerState.PATH_LENGTH;

    private ChannelHandlerContext ctx;
    private ByteBuf buf;
    private String root;

    private int pathLength;
    private String pathString;

    public DownloadHandler(ChannelHandlerContext ctx, String root) {
        this.ctx = ctx;
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
            Path path = Path.of(pathString);
            FileChannel fileChannel = FileChannel.open(path); // to do: check path.contains(root) // violation
            ByteBuf fileBuf = ByteBufAllocator.DEFAULT.buffer(pathLength);
            fileBuf.writeBytes(fileChannel, pathLength);
            ctx.channel().flush();
            ctx.channel().writeAndFlush(fileBuf);
            state = DownloadHandlerState.COMPLETE;
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
