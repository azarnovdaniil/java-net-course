package ru.daniilazarnov.handler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import ru.daniilazarnov.domain.MyMessage;

public class AuthHandler extends ChannelInboundHandlerAdapter {
    private boolean auth = false;
    private final int maxLenght = 3;

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        System.out.println("Client connected...");
        ctx.writeAndFlush(new MyMessage("Wellcome to storage"));
        ctx.writeAndFlush(new MyMessage("To get started, register in the repository or log in, enter /help"));
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        System.out.println("Client disconnected...");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (auth) {
            ctx.fireChannelRead(msg);
            return;
        } else {
            if (msg instanceof MyMessage) {
                String identificator = ((MyMessage) msg).getText();
                String[] ident = identificator.split(" ");
                if (ident[0].equals("/auth") && ident.length == maxLenght) {
                    ctx.writeAndFlush(new MyMessage((new CommandServer()).authInStorage(ident[1], ident[2])));
                    ctx.pipeline().addLast(new MainHandler());
                    auth = true;
                } else if (ident[0].equals("/reg") && ident.length == maxLenght) {
                    ctx.writeAndFlush(new MyMessage((new CommandServer()).regInStorage(ident[1], ident[2])));
                } else if (ident[0].equals("/help")) {
                    ctx.writeAndFlush(new MyMessage((new CommandServer()).callHelpManual().toString()));
                }                else {
                    ctx.writeAndFlush(new MyMessage("Wrong command, see /help"));
                }
            } else {
                ctx.writeAndFlush(new MyMessage("Wrong type data, see /help"));
            }
        }
    }
}
