package ru.johnnygomezzz.handlers;

import ru.johnnygomezzz.FileMessage;
import ru.johnnygomezzz.FileRequest;
import ru.johnnygomezzz.MyMessage;
import ru.johnnygomezzz.download.DownLoad;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import ru.johnnygomezzz.upload.UpLoad;

public class ServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Клиент подключился");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        System.out.println("Клиент отключился");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)
            throws Exception {

        if (msg instanceof FileRequest) {
            UpLoad.upLoad(ctx, msg);
        }

        if (msg instanceof FileMessage) {
            DownLoad.download(msg);
        }

        if (msg instanceof MyMessage) {
            String message = ((MyMessage) msg).getText();
            System.out.println("Сообщение от клиента: " + message);

            if (message.equals("/quit")) {
                System.exit(0);
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
