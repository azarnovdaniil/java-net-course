package commands;

import io.netty.channel.ChannelHandlerContext;
import ru.daniilazarnov.MyMessage;
import server.ServerHandler;

import java.io.File;

public class Rename extends ServerHandler {


    public static void renameFileFromServer(String account, ChannelHandlerContext ctx, String oldNameFile,
                                            String newNameFile) {
        File file = new File(WAY_SERVER + account, oldNameFile);
        File newFile = new File(WAY_SERVER + account, newNameFile);

        if (file.renameTo(newFile)) {
            ctx.writeAndFlush(new MyMessage("Файл " + file + " успешно переименован в " + newFile));
        } else {
            ctx.writeAndFlush(new MyMessage("Файл " + file + " НЕ переименован в " + newFile));
        }
    }
}
