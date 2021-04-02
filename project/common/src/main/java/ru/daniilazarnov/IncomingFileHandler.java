package ru.daniilazarnov;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class IncomingFileHandler<T extends PathHolder> extends ChannelInboundHandlerAdapter {
    private final T pathHolder;

    IncomingFileHandler(T pathHolder) {
        this.pathHolder = pathHolder;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        FileContainer container = (FileContainer) msg;
        Path filePath = Paths.get(pathHolder.getAuthority(), container.getName());

        if (!filePath.toFile().exists()) {
            Files.createFile(filePath);
        }
        RandomAccessFile rnd = new RandomAccessFile(filePath.toString(), "rw");
        rnd.seek(rnd.length());
        rnd.write(container.getFilePart());
        rnd.close();
        if (filePath.toFile().length() == pathHolder.getFileLength()) {
            pathHolder.transComplete();
        }

    }
}
