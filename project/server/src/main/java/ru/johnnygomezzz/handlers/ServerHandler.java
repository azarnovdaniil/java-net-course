package ru.johnnygomezzz.handlers;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import ru.johnnygomezzz.MyMessage;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ServerHandler extends ChannelInboundHandlerAdapter {
    private final String PATH = Path.of("project/server/storage/").toString();

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

        if (message.startsWith("/touch") && messagePart.length > 1) {
            Path tempPath = Path.of(PATH, messagePart[1]);
            if (Files.exists(tempPath)) {
                textMessage = new MyMessage("Файл с именем " + messagePart[1] + " уже существует.");
                ctx.writeAndFlush(textMessage);
            }

            else if (messagePart.length == 2){
                Paths.get(PATH, messagePart[1]);
                textMessage = new MyMessage("Файл " + messagePart[1] + " успешно создан.");
                ctx.writeAndFlush(textMessage);
            }

            else {
                Path path = Paths.get(PATH, messagePart[1]);
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
        }

        else {
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
