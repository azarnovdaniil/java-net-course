package ru.johnnygomezzz.handlers;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import ru.johnnygomezzz.MyMessage;

public class ServerHandler extends ChannelInboundHandlerAdapter {
    private MyMessage textMessage;

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        System.out.println("Клиент подключился");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        System.out.println("Клиент отключился");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {

            String message = ((MyMessage) msg).getText();
            System.out.println("Сообщение от клиента: " + message);

        if (!message.startsWith("/")) {
            textMessage = new MyMessage(message + " не является командой. Введите команду:");
            ctx.writeAndFlush(textMessage);
        }
        if (message.equals("/quit")) {
            textMessage = new MyMessage("/quit");
            ctx.writeAndFlush(textMessage);
            System.exit(0);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
