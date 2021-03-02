package commands;

import ru.daniilazarnov.FileMessage;
import ru.daniilazarnov.FileRequest;
import ru.daniilazarnov.MyMessage;
import server.ServerHandler;


import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Paths;

public class UpLoad extends ServerHandler {

    public static void upLoad(io.netty.channel.ChannelHandlerContext ctx, java.lang.Object msg) {
        FileRequest fr = (FileRequest) msg;
        try {
            if (Files.exists(Paths.get(WAY_SERVER + fr.getAccount() + fr.getFilename()))) {

                FileMessage fm = new FileMessage(Paths.get(WAY_SERVER + fr.getAccount() + fr.getFilename()));
                ctx.writeAndFlush(fm);
                System.out.println("отправлен " + fm.getFileName());
            } else {
                ctx.writeAndFlush(new MyMessage("Файл " + fr.getFilename() + " отсутствует на сервере"));
            }
        } catch (IOException e) {
            System.err.println("Файл отсутствует");
        }
    }
}
