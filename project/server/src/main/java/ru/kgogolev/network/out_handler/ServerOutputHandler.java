package ru.kgogolev.network.out_handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import ru.kgogolev.FileEncoder;

import java.nio.file.Path;

public class ServerOutputHandler extends ChannelOutboundHandlerAdapter {
    private final FileEncoder fileEncoder;

    public ServerOutputHandler(FileEncoder fileEncoder) {
        this.fileEncoder = fileEncoder;
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {

        ByteBuf buffer = (ByteBuf) msg;

        if (buffer.readByte() != FileEncoder.MAGIC_BYTE) {
            buffer.resetReaderIndex();
            ctx.writeAndFlush(buffer);

        } else {
            ctx.writeAndFlush(fileEncoder.sendFile(Path.of("D:", "302.jpg")));
        }
    }
}
