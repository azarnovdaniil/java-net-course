
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import ru.daniilazarnov.FileMessage;
import ru.daniilazarnov.FileRequest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public class ServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Клиент подключился ");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        System.out.println("Клиент отключился");
    }

    @Override
    public void channelRead(io.netty.channel.ChannelHandlerContext ctx, java.lang.Object msg)
            throws java.lang.Exception {


        // Отправка пакета на клиент

        if (msg instanceof FileRequest) {

            FileRequest fr = (FileRequest) msg;

            if (Files.exists(Paths.get("project", "server", "src", "main", "java", "file/"
                    + fr.getFilename()))) {
                downLoad(ctx, fr);
            }

        }

        // Прием пакета от клиента




    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    public void search(io.netty.channel.ChannelHandlerContext ctx, java.lang.String files) {
        Path path = Paths.get("project", "server", "src", "main", "java", "file", files);
        ctx.writeAndFlush(Files.exists(path));
    }

    public void downLoad(io.netty.channel.ChannelHandlerContext ctx, FileRequest fr) throws IOException {
        FileMessage fm = new FileMessage(Paths.get("project", "server", "src", "main", "java",
                "file/" + fr.getFilename()));
        ctx.writeAndFlush(fm);
        System.out.println("скачал");
    }
}
