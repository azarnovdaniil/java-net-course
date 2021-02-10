package clientserver.commands;

import clientserver.FileLoaded;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.util.Map;

public interface Command {
    void send(ChannelHandlerContext ctx, String content, byte signal);

    void response(ChannelHandlerContext ctx, ByteBuf buf, String currentDir, Map<Integer, FileLoaded> uploadedFiles);

    void receive(ChannelHandlerContext ctx, ByteBuf buf);
}
