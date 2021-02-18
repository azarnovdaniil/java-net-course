package ru.daniilazarnov;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import ru.daniilazarnov.FileService.FileManager;
import java.nio.file.Path;

/**
 * cachedInComingMessage - кэыширование поступившего пакета для возможного использования (функционал пока не реализован)
 * FileManager - общий класс для работы с файловой системой. Часть его полей  совпадают с полями класса  MessagePacket, но отличаются методы.
 */

public class DServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        System.out.println("Client connected...");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        System.out.println("Client disconnected...");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {

        if (msg instanceof MessagePacket) {
            MessagePacket mp = (MessagePacket) msg;
            FileManager fileManager = new FileManager(mp.getCommand(), Path.of(mp.getPathCode()), mp.getHomeDirectory(), mp.getFileName(), mp.getContent(), mp.getSegment(), mp.getAllSegments());

            if (!fileManager.runner()) {
                ctx.close();
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
