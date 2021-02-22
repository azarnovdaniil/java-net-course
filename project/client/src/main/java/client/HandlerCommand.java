package client;

import common.Commands.commandsImplements.Disconnect;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import common.service.FileLoad;
import common.service.FileLoadService;

public class HandlerCommand extends ChannelInboundHandlerAdapter {


    public static ChannelHandlerContext ctx;
    private FileLoadService fileLoadService = new FileLoadService();



    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        this.ctx = ctx;

        System.out.println("Готов принимать и отравлять команды" + ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {

        if (msg instanceof FileLoad) {
                try {
                    PrBar.countParts = ((FileLoad) msg).getCountParts();
                    PrBar.countProgress =((FileLoad) msg).getCountProgress();

                    fileLoadService.readFile(((FileLoad) msg), ctx);
                } catch (Exception e) {
                    e.printStackTrace();
                }
        }

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

    public void Disconnect() {
        ctx.writeAndFlush(new Disconnect());
    }
}
