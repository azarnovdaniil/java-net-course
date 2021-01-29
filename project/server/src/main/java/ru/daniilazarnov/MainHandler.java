package ru.daniilazarnov;

/*
  Это главный (первый "на входе") обработчик пакетов данных, приходящих на Сервер.
  */

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class MainHandler extends ChannelInboundHandlerAdapter {
    //переопределяем необходимые нам методы
    //метод, отвечающий за активацию канала (при подключении клиента)
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("К серверу подключился новый клиент.");
    }

    //метод, отвечающий за деактивацию канала (при самостоятельном отключении клиента)
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Клиент отключился от сервера.");
        ctx.close();
        System.out.println("Соединение с клиентом закрыто.");
    }
    //срабатывает, когда клиент прислал сообщение (обернуютое в буфер байтов -ByteBuf).
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg; //явное приведения типа к буферу байтов
        while (buf.readableBytes() >0) {

            System.out.print((char) buf.readByte());
        }
        //после того, как закончили обработку буфера, буфер очищаем
        buf.release();
    }
    //перехват исключений
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace(); //выводим стэктрэйс
        ctx.close(); // закрываем соединение с клиентом
    }
}
