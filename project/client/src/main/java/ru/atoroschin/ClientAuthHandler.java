package ru.atoroschin;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.TimeUnit;

public class ClientAuthHandler extends ChannelInboundHandlerAdapter {
    private final String fileCredentials = "prop.crd";
    private Credentials credentials;

    @Override
    public void channelActive(ChannelHandlerContext channelHandlerContext) {
        if (Files.exists(Path.of(fileCredentials))) {
            readAuth(channelHandlerContext, new FileReadCredentials(fileCredentials));
        } else {
            readAuth(channelHandlerContext, new ConsoleReadCredentials());
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext channelHandlerContext, Object msg) {
        ByteBuf buf = (ByteBuf) msg;
        if (buf.readableBytes() > 0) {
            byte firstByte = buf.readByte();
            CommandsAuth command = CommandsAuth.getCommand(firstByte);
            command.receive(channelHandlerContext, buf, null);
            if (command.equals(CommandsAuth.AUTHOK)) {
                System.out.println("Авторизация прошла успешно.");
                System.out.println("Для справки используйте команду help");
                new FileReadCredentials(fileCredentials).write(credentials);
                channelHandlerContext.channel().pipeline().remove(this);
                channelHandlerContext.fireChannelActive();
            } else {
                System.out.println("Неверный логин или пароль");
                readAuth(channelHandlerContext, new ConsoleReadCredentials());
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext channelHandlerContext, Throwable cause) {
        cause.printStackTrace();
        channelHandlerContext.close();
    }

    public void readAuth(ChannelHandlerContext ctx, ReadCredentials readCredentials) {
        try {
            credentials = readCredentials.read();

            CommandsAuth command = CommandsAuth.AUTHLOGIN;
            command.sendToServer(ctx, credentials);

            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }
}
