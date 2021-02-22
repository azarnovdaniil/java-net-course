package download;

import ru.daniilazarnov.FileMessage;
import ru.daniilazarnov.FileRequest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class DownLoad {
    private static final String WAY_SERVER = ("project/server/src/main/java/file/");

    public static void upLoad(io.netty.channel.ChannelHandlerContext ctx, java.lang.Object msg) throws IOException {
        FileRequest fr = (FileRequest) msg;
        if (Files.exists(Paths.get(WAY_SERVER + fr.getFilename()))) {
            FileMessage fm = new FileMessage(Paths.get(WAY_SERVER + fr.getFilename()));
            ctx.writeAndFlush(fm);
            System.out.println("получил");
        }
    }
}
