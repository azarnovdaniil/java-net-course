package ru.ivanverkhovskiy.handlers;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ClientHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Клиент подключился ");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        System.out.println("Клиент отключился");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        Path path = Paths.get(
                "/Users/admin/Desktop/Java/GB/NetworkStorage/java-net-course/project/server/src/main/resources/Test.txt");
        String str = "Кек: запись сделана!";
        byte[] bs = str.getBytes();
        Path writtenFilePath = Files.write(path, bs);
        System.out.println("Записано в файл:\n"+ new String(Files.readAllBytes(writtenFilePath)));
        if (msg.equals("/show"))
        {
            System.out.println(path.toString());
        }
        else {
            System.out.println("Сообщение от клиента: " + msg);
            ctx.writeAndFlush(msg);
        }
        if (msg.equals("/quit"))
        {
            ctx.close();
        }
    }
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}