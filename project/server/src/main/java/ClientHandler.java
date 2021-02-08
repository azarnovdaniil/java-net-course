import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.ReferenceCountUtil;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public class ClientHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Клиент подключился ");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        System.out.println("Клиент отключился");
    }

    @Override
    public void channelRead(io.netty.channel.ChannelHandlerContext ctx, java.lang.Object msg) throws java.lang.Exception {





//        String buf = (String) msg;


        if (msg.equals("/all")) {
            Path path = Paths.get("project", "server","src","main","java","file","1.txt");
//            path.resolve("654654.txt");

            ctx.writeAndFlush(Files.exists(path));
        }

    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
