package clientserver.commands;

import clientserver.FileLoaded;
import clientserver.FileWorker;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;

import java.util.Map;

public class CommandExit implements Command {
    @Override
    public void send(ChannelHandlerContext ctx, String content, FileWorker fileWorker, byte signal) {
        ByteBuf byBuf = ByteBufAllocator.DEFAULT.buffer();
        byBuf.writeByte(signal);
        byBuf.writeInt(5);
        ctx.writeAndFlush(byBuf);
        ctx.fireChannelUnregistered();
        ctx.channel().close();
    }

    @Override
    public void response(ChannelHandlerContext ctx, ByteBuf buf, FileWorker fileWorker, Map<Integer, FileLoaded> uploadedFiles, byte signal) {
        ctx.fireChannelUnregistered();
        ctx.channel().close();
    }

    @Override
    public void receive(ChannelHandlerContext ctx, ByteBuf buf, FileWorker fileWorker, Map<Integer, FileLoaded> uploadedFiles) {

    }
}
