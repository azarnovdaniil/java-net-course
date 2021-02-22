package delete;

import ru.daniilazarnov.MyMessage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Delete {
    private static final String WAY_SERVER = ("project/server/src/main/java/file/");

    public static void delete(String message,String [] messageCommand,io.netty.channel.ChannelHandlerContext ctx, java.lang.Object msg){
        System.out.println(message);
            try {
                Path deletePath = Paths.get(WAY_SERVER + messageCommand[1]);
                Files.delete(deletePath);
                ctx.writeAndFlush(new MyMessage("Файл успешно удален на сервере"));
            } catch (IOException e) {
                ctx.writeAndFlush("НЕ УДАЛОСЬ УДАЛИТЬ ФАЙЛ НА СЕРВЕРЕ");
            }
        }
    }

