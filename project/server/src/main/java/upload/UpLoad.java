package upload;


import ru.daniilazarnov.FileMessage;
import ru.daniilazarnov.FileRequest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class UpLoad {
    private static final String WAY = ("project/server/src/main/java/file/");

    public static void upLoad(io.netty.channel.ChannelHandlerContext ctx, java.lang.Object msg) throws IOException {
        FileRequest fr = (FileRequest) msg;
        if (Files.exists(Paths.get(WAY + fr.getFilename()))) {
            FileMessage fm = new FileMessage(Paths.get(WAY + fr.getFilename()));
            ctx.writeAndFlush(fm);
            System.out.println("получил");
        }
    }
}
