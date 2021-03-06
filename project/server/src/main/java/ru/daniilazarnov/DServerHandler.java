package ru.daniilazarnov;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import ru.daniilazarnov.commands.Commands;
import ru.daniilazarnov.commands.Exit;

/**
 * cachedInComingMessage - кэыширование поступившего пакета для возможного использования (функционал пока не реализован)
 * FileManager - общий класс для работы с файловой системой. Часть его полей  совпадают с полями класса  MessagePacket, но отличаются методы.
 */

public class DServerHandler extends ChannelInboundHandlerAdapter {


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
        Commands commands;
        if (msg instanceof MessagePacket) {
            MessagePacket mp = (MessagePacket) msg; //привели к нашему объекту
            commands = mp.getCommand(); //создали объект команды
            if (commands instanceof Exit) { //закрываем соединение на случай команды *Exit
                ctx.close();
            } else {
                MessagePacket answer = commands.runCommands(mp); //реализовали метод команды, возвращающей ответный пакет
                ctx.writeAndFlush(answer); //вернули пакет пользователю
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
