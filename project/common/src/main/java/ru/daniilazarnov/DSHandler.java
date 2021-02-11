package ru.daniilazarnov;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import ru.daniilazarnov.FileService.FileManager;

/**
 * cachedInComingMessage - кэыширование поступившего пакета для возможного использования (функционал пока не реализован)
 * */

public class DSHandler extends ChannelInboundHandlerAdapter {

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
        System.out.println(msg.getClass().getName());
        if (msg instanceof MessagePacket) {

            MessagePacket inComingMessage = (MessagePacket) msg;
            MessagePacket cachedInComingMessage = new MessagePacket();
            cachedInComingMessage.setCommandToServer(inComingMessage.getCommandToServer());
            cachedInComingMessage.setPathToFileName(inComingMessage.getPathToFileName());
            cachedInComingMessage.setSegment(inComingMessage.getSegment());
            cachedInComingMessage.setAllSegments(inComingMessage.getAllSegments());
            cachedInComingMessage.setContent(inComingMessage.getContent());
            FileManager fileManager=new FileManager(inComingMessage);
fileManager.fileAction();
            //ctx.writeAndFlush(inComingMessage);
        }

    }




    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
