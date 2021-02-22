package server;


import common.Commands.commandsImplements.Disconnect;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import common.service.FileLoad;
import common.service.FileLoadService;


public class HandlerCommand extends ChannelInboundHandlerAdapter {

    private FileLoadService fileLoadService = new FileLoadService();

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        System.out.println("Клиент channelActive: " + ctx);

    }


    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Клиент channelInactive: " + ctx);
        ctx.close();
    }


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        if (msg instanceof FileLoad) {
            try {

                fileLoadService.writeFile(((FileLoad)msg),ctx);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (msg instanceof Disconnect) {
                System.out.println("закрыли канал");
                ctx.channel().close();
        }

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        System.out.println("Клиент отключился");
        ctx.close();
    }
}
