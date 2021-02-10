package clientserver.commands;

import clientserver.FileLoaded;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.util.Map;

public class CommandUnknown implements Command {

    @Override
    public void send(ChannelHandlerContext ctx, String content, byte signal) {
        System.out.println("Неизвестная команда");
    }

    @Override
    public void response(ChannelHandlerContext ctx, ByteBuf buf, String currentDir, Map<Integer, FileLoaded> uploadedFiles) {
        // сервер ничего не делает
    }

    @Override
    public void receive(ChannelHandlerContext ctx, ByteBuf buf) {
        System.out.println("Неизвестная команда");
    }
}
