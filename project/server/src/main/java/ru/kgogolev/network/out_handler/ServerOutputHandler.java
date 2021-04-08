package ru.kgogolev.network.out_handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import ru.kgogolev.FileEncoder;

import java.nio.file.Path;

public class ServerOutputHandler extends ChannelOutboundHandlerAdapter {
    private FileEncoder fileEncoder;

    public ServerOutputHandler(FileEncoder fileEncoder) {
        this.fileEncoder = fileEncoder;
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        ctx.writeAndFlush(fileEncoder.sendFile(Path.of("D:", "302.jpg")));
    }
}
