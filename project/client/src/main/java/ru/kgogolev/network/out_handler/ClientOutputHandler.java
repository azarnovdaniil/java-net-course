package ru.kgogolev.network.out_handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.util.CharsetUtil;
import ru.kgogolev.FileEncoder;
import ru.kgogolev.StringConstants;

import java.nio.file.Path;


public class ClientOutputHandler extends ChannelOutboundHandlerAdapter {
    private FileEncoder fileEncoder;

    public ClientOutputHandler(FileEncoder fileEncoder) {
        this.fileEncoder = fileEncoder;
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        ByteBuf inBuffer = (ByteBuf) msg;
        String input = inBuffer.toString(CharsetUtil.UTF_8);
        if (input.startsWith(StringConstants.UPLOAD)) {
            ctx.writeAndFlush(fileEncoder.sendFile(Path.of(input.split(" ")[1])));
        } else {
            ctx.writeAndFlush(msg);
        }
    }
}
