package ru.johnnygomezzz.handlers;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import ru.johnnygomezzz.MyMessage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class ServerHandler extends ChannelInboundHandlerAdapter {
    private final String path = Path.of("project/server/storage/").toString();

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        System.out.println("Клиент подключился");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        System.out.println("Клиент отключился");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {

            String message = ((MyMessage) msg).getText();
            System.out.println("Сообщение от клиента: " + message);

            String[] messagePart = message.split("\\s");

        MyMessage textMessage;

        if (message.startsWith("/quit")) {
            textMessage = new MyMessage("/quit");
            ctx.writeAndFlush(textMessage);
            System.exit(0);
        }

        if (message.startsWith("/ls") && messagePart.length > 1) {
            File dir = new File(path, messagePart[1]);
            File[] arrFiles = dir.listFiles();
            List<File> list = Arrays.asList(arrFiles);
            textMessage = new MyMessage(String.valueOf(list));
            ctx.writeAndFlush(textMessage);
        }

        if (message.startsWith("/touch") && messagePart.length > 1) {
            if (Files.exists(Path.of(path, messagePart[1]))) {
                textMessage = new MyMessage("Файл с именем " + messagePart[1] + " уже существует.");
                ctx.writeAndFlush(textMessage);
            } else if (messagePart.length == 2) {
                Paths.get(path, messagePart[1]);
                textMessage = new MyMessage("Файл " + messagePart[1] + " успешно создан.");
                ctx.writeAndFlush(textMessage);
            } else {
                Path path = Paths.get(this.path, messagePart[1]);
                try {
                    String str = messagePart[2];
                    byte[] bs = str.getBytes();
                    Path writtenFilePath = Files.write(path, bs);
                    textMessage = new MyMessage("Записано в файл " + messagePart[1] + "\n"
                            + new String(Files.readAllBytes(writtenFilePath)));
                    ctx.writeAndFlush(textMessage);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return;
        }

        if (message.startsWith("/delete") && messagePart.length > 1) {
            if (Files.exists(Path.of(path, messagePart[1]))) {
                try {
                    Files.delete(Paths.get(path, messagePart[1]));
                    textMessage = new MyMessage("Файл с именем " + messagePart[1] + " успешно удалён.");
                    ctx.writeAndFlush(textMessage);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                textMessage = new MyMessage("Файл с именем " + messagePart[1] + " отсутствует.");
                ctx.writeAndFlush(textMessage);
            }
        } else {
            textMessage = new MyMessage("\"" + message + "\""
                    + " неполное значение или не является командой.\nВведите команду:");
            ctx.writeAndFlush(textMessage);
        }
    }
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
