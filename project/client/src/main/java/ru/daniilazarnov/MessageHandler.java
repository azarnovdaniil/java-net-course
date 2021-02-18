package ru.daniilazarnov;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.Locale;
//
public class MessageHandler {
    Channel ch;

    public MessageHandler(Channel ch) throws IOException {
        this.ch = ch;
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Введите команду:");
            while (true) {
                toProcessMessage(in.readLine(), ch);
            }
    }

    public void toProcessMessage(String message, Channel ch) throws IOException {
        String command = (message.contains(" ") ? message.split(" ")[0] : message).toLowerCase(Locale.ROOT);
        ByteBuf byteBuf = ByteBufAllocator.DEFAULT.buffer();
        switch (command) {
            case "-ls":
                byteBuf.writeByte(Command.LS.getSignal());
                ch.writeAndFlush(byteBuf);
                break;
            case "-help":
                byteBuf.writeByte(Command.HELP.getSignal());
                ch.writeAndFlush(byteBuf);
                break;
            case "-cd":
                String CD = message.contains(" ") ? (" " + message.split(" ")[1]) : "";
                byteBuf.writeByte(Command.CD.getSignal());
                byteBuf.writeBytes(CD.getBytes());
                System.out.println(byteBuf.toString(StandardCharsets.UTF_8));
                ch.writeAndFlush(byteBuf);
                break;
            case "-reg":
                String loginReg = " " + (message.contains(" ") ? message.split(" ")[1] : message) + " ";
                String passwordReg = message.contains(" ") ? message.split(" ")[2] : message;
                byteBuf.writeByte(Command.REG.getSignal());
                byteBuf.writeBytes(loginReg.getBytes());
                byteBuf.writeBytes(passwordReg.getBytes());
                ch.writeAndFlush(byteBuf);
                break;
            case "-auth":
                String login = " " + (message.contains(" ") ? message.split(" ")[1] : message) + " ";
                String password = message.contains(" ") ? message.split(" ")[2] : message;
                byteBuf.writeByte(Command.AUTH.getSignal());
                byteBuf.writeBytes(login.getBytes());
                byteBuf.writeBytes(password.getBytes());
                ch.writeAndFlush(byteBuf);
                break;
            case "-mkdir":
                String newDirectory = " " + (message.contains(" ") ? message.split(" ")[1] : message);
                byteBuf.writeByte(Command.MKDIR.getSignal());
                byteBuf.writeBytes(newDirectory.getBytes());
                ch.writeAndFlush(byteBuf);
                break;
            case "-upload":
                Path path = Paths.get(message.split(" ")[1]);
                FileRegion region = new DefaultFileRegion(path.toFile(), 0, Files.size(path));
                byte[] filenameBytes = path.getFileName().toString().getBytes(StandardCharsets.UTF_8);
                ByteBuf buf = ByteBufAllocator.DEFAULT.directBuffer(1+4+filenameBytes.length + 8);
                buf.writeByte(Command.UPLOAD.getSignal());
                buf.writeInt(filenameBytes.length);
                buf.writeBytes(filenameBytes);
                buf.writeLong(Files.size(path));
                ch.writeAndFlush(buf);
                ch.writeAndFlush(region);
                break;
            case "-download":
                String file = " " + (message.contains(" ") ? message.split(" ")[1] : message);
                byteBuf.writeByte(Command.DOWNLOAD.getSignal());
                byteBuf.writeBytes(file.getBytes());
                ch.writeAndFlush(byteBuf);
                break;
            case "-rm":
                String fileDelete = " " + (message.contains(" ") ? message.split(" ")[1] : message);
                byteBuf.writeByte(Command.RM.getSignal());
                byteBuf.writeBytes(fileDelete.getBytes());
                ch.writeAndFlush(byteBuf);
                break;
            case "-rn":
                String fileRename = " " + (message.contains(" ") ? message.split(" ")[1] : message) + " " +(message.contains(" ") ? message.split(" ")[2] : message);
                byteBuf.writeByte(Command.RN.getSignal());
                byteBuf.writeBytes(fileRename.getBytes());
                ch.writeAndFlush(byteBuf);
                break;
            default:
                System.out.println("Неверная команда");
                break;

        }
    }
}



