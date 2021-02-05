package ru.daniilazarnov;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Первичный обработчик
 * Проверяем содержимое заголовка
 */
public class FirstHandler extends ChannelInboundHandlerAdapter {
    private static final Logger log = Logger.getLogger(FirstHandler.class);
    private static final List<Channel> channels = new ArrayList<>();
    private static List<User> listOfUsers = new ArrayList<>();
    private static int newClientIndex = 1;
    private String clientName;


    // Что делать, когда к нам прилетело сообщение?
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // Поскольку этот хендлер стоит "первым от сети", то 100% получим ByteBuf
        ByteBuf buf = (ByteBuf)msg;
        // Ждем получения трех байт
//        if (buf.readableBytes() < 3) {
//            return;
//        }
        // Как только получили три байта, готовим массив, чтобы их туда закинуть
        byte[] data = new byte[buf.readableBytes()];


//        if (){
//
//        }


        // Перекидываем байты из буфера в массивЯ
        buf.readBytes(data);
            log.info("[SERVER: Сообщение принято от " + listOfUsers.get(listOfUsers.size()-1)+
                    "]");
            ctx.writeAndFlush("FirstHandler пройден " + new String(data));
//            ctx.fireChannelRead(data);
            buf.release();


        // Освобождаем буфер
//        buf.release();
        // Прокидываем массив дальше по конвееру
        ctx.fireChannelRead(data);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        channels.add(ctx.channel());
        clientName = "Клиент #" + newClientIndex;
        listOfUsers.add(new User(ctx.channel(), clientName, "user" + newClientIndex));
        log.info("SERVER: Клиент подключился: " + clientName);
//        System.out.println("SERVER: Клиент подключился: " + ctx.name());
        newClientIndex++;
    }

    // Стандартный обработчик исключений. Не забывайте его добавлять!!!
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
