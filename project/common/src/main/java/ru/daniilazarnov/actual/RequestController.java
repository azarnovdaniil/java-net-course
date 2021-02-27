package ru.daniilazarnov.actual;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.*;

import java.io.*;
import java.nio.file.Paths;

public class RequestController {
    private Channel channel;
    private Commands command;

    public RequestController(Channel channel) {
        this.channel = channel;
        command = new Commands();
    }

    public void command(String string) throws IOException {
        String[] cmd = string.split("\\s", 2);
        String part1 = cmd[0];
        String part2;

        ByteBuf buf = ByteBufAllocator.DEFAULT.directBuffer(1);
        switch (part1.toUpperCase()) {
            case "HELP":
                command.help();
                break;
            case "REG":
                part2 = cmd[1]; //login & password
                buf.writeByte(Signals.REG.get());
                buf.writeBytes(Utils.convertToByteBuf(part2));
                channel.writeAndFlush(buf);
                break;
            case "AUTH":
                part2 = cmd[1]; //login & password
                buf.writeByte(Signals.AUTH.get());
                buf.writeBytes(Utils.convertToByteBuf(part2));
                channel.writeAndFlush(buf);
                break;
            case "LS":
                buf.writeByte(Signals.LS.get());
                channel.writeAndFlush(buf);
                break;
            case "RM":
                part2 = cmd[1]; //file name
                buf.writeByte(Signals.RM.get());
                buf.writeBytes(Utils.convertToByteBuf(part2));
                channel.writeAndFlush(buf);
                break;
            case "UPL": {
                part2 = cmd[1]; //full path to file
                command.sendFile(Paths.get(part2), channel, future -> {
                    if (!future.isSuccess()) {
                        System.out.println("Ошибка при отправке файла.");
                        future.cause().printStackTrace();
                    }
                    if (future.isSuccess()) {
                        System.out.println("Файл успешно отправлен.");
                    }
                });
                break;
            }
            case "DOWN": {
                buf.writeByte(Signals.DOWNLOAD.get());
                channel.writeAndFlush(buf);
                break;
            }
            default: {
                System.out.println("Команда не распознана. Воспользуйтесь командой help для справки...");
            }
        }
    }
}
