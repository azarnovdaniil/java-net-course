package commands;

import io.netty.channel.ChannelHandlerContext;
import ru.daniilazarnov.MyMessage;
import server.ServerHandler;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Delete extends ServerHandler {

    public static void delete(String account, String message, String messageCommand, ChannelHandlerContext ctx,
                              Object msg) {
        System.out.println(message);

            try {

                Path deletePath = Paths.get(WAY_SERVER + account + messageCommand);
                Files.delete(deletePath);
                ctx.writeAndFlush(new MyMessage("Файл " + messageCommand + " успешно удален на сервере"));
            } catch (IOException e) {
                ctx.writeAndFlush(new MyMessage("Файл " + messageCommand + " отсутствует на сервере"));
            }
        }
    }

