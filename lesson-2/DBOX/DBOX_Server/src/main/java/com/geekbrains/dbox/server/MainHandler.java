package com.geekbrains.dbox.server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import javax.print.DocFlavor;
import java.util.ArrayList;

public class MainHandler extends ChannelInboundHandlerAdapter {

    @Override
    public  void  channelActive(ChannelHandlerContext ctx) throws Exception{
        //Подключение клиента
        System.out.println("Клиент подключился");
    }

    @Override
    public  void  channelRead(ChannelHandlerContext ctx, Object msg) throws Exception{
        //Получаем  данные в ByteBuf
        ByteBuf buf = (ByteBuf)msg;
        //Побайтово обрабатываем данные
        String str = "";
        while (buf.readableBytes() > 0) {
             str = str + (char) buf.readByte();
        }
        //очищаем буфер Netty
        buf.release();

        //1 - Авторизация
        System.out.print(str);
    }

    @Override
    //Возникновение исключения
    public  void  exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception{
        //Вывод исключения в консоль
        cause.printStackTrace();
        //закрытие соединения
        ctx.close();

    }
}
