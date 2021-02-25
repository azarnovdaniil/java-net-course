package delete;

import io.netty.channel.ChannelHandlerContext;
import ru.daniilazarnov.MyMessage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Delete {
    private static final String WAY_SERVER = ("project/server/src/main/java/file/");

    public static void delete(String message, String messageCommand, ChannelHandlerContext ctx, Object msg) {
        System.out.println(message);
            try {
                Path deletePath = Paths.get(WAY_SERVER + messageCommand);
                Files.delete(deletePath);
                ctx.writeAndFlush(new MyMessage("Файл " + messageCommand + " успешно удален на сервере"));
            } catch (IOException e) {
                ctx.writeAndFlush(new MyMessage("Файл " + messageCommand + " отсутствует на сервере"));
            }
        }
    }

