package ru.daniilazarnov;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.apache.log4j.Logger;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *Нереализованный обработчик
 *
 */
public class FirstHandler extends ChannelInboundHandlerAdapter {
    private static final Logger log = Logger.getLogger(FirstHandler.class);
    private static final List<Channel> channels = new ArrayList<>();
    private static List<User> listOfUsers = new ArrayList<>();
    private static int newClientIndex = 1;
    private String clientName;



    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg){
            ByteBuf buf = (ByteBuf)msg;

        if (buf.readableBytes() > 0) {
            byte[] data = new byte[buf.readableBytes()];
            buf.readBytes(data);
            String s =  "[(FirstHandler) SERVER: " + new String(data) + "]";


            buf = ByteBufAllocator.DEFAULT.directBuffer(4);
            buf.writeInt(s.length());
            ctx.write(buf);
            byte[] stringSource = s.getBytes();
            buf = ByteBufAllocator.DEFAULT.directBuffer(s.length());
            buf.writeBytes(stringSource);
            ctx.write(buf);
            ctx.flush();
            buf.release(); // Освобождаем буфер
            ctx.fireChannelRead(data);
        }

    }

    @Override
    public void channelActive(ChannelHandlerContext ctx){
        channels.add(ctx.channel());
        clientName = "Клиент #" + newClientIndex;
        listOfUsers.add(new User(ctx.channel(), clientName, "user" + newClientIndex));
        log.info("SERVER: Клиент подключился: " + clientName);
//        System.out.println("SERVER: Клиент подключился: " + ctx.name());
        newClientIndex++;
        ctx.writeAndFlush("Пользователь" + clientName +
                "подключился ");
    }

    // Стандартный обработчик исключений.
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause){
        cause.printStackTrace();
        ctx.close();
    }
}
