package com.geekbrains.dbox.server.handlers.in;

import com.geekbrains.dbox.server.handlers.Message;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class HandlerInReadBytes extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //Подключение клиента
        System.out.println("Клиент подключился");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //Получаем  данные в ByteBuf
        ByteBuf buf = (ByteBuf) msg;

        byte[] bytes = new byte[buf.readableBytes()];
        int readerIndex = buf.readerIndex();
        buf.getBytes(readerIndex, bytes);

        Message m = new Message();
        m.HandlerInToMess(bytes);
//        System.out.println(m.com + " <> " + m.obj.toString());

        ctx.fireChannelRead(m);

        //очищаем буфер Netty
        buf.release();

    }

    @Override
    //Возникновение исключения
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        //Вывод исключения в консоль
        cause.printStackTrace();
        //закрытие соединения
        ctx.close();

    }
}