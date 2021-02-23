package ru.atoroschin.commands;

import ru.atoroschin.Commands;
import ru.atoroschin.FileLoaded;
import ru.atoroschin.FileWorker;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.util.Arrays;
import java.util.Map;

public class CommandHelp implements Command {
    @Override
    public void send(ChannelHandlerContext ctx, String content, FileWorker fileWorker, byte signal) {
        System.out.println();
        System.out.println("Приложение предоставляет доступ к облачному хранилищу.");
        Arrays.stream(Commands.values())
                .filter(commands -> commands.getSignal() != Byte.MIN_VALUE)
                .map(command -> command.name() + command.getDescription())
                .forEach(System.out::println);
        System.out.println("Написание команд не чувствительно к регистру.");
    }

    @Override
    public void response(ChannelHandlerContext ctx, ByteBuf buf, FileWorker fileWorker, Map<Integer,
            FileLoaded> uploadedFiles, byte signal) {
    }

    @Override
    public void receive(ChannelHandlerContext ctx, ByteBuf buf, FileWorker fileWorker, Map<Integer,
            FileLoaded> uploadedFiles) {
    }
}
