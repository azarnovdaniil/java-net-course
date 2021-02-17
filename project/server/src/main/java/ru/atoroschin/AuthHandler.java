package ru.atoroschin;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import ru.atoroschin.auth.BaseAuthService;

import java.util.List;

public class AuthHandler extends ChannelInboundHandlerAdapter {
    private final AuthService authService;
    private String login;

    public AuthHandler() {
        this.authService = new BaseAuthService();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg;
        if (buf.readableBytes() > 0) {
            byte b = buf.readByte();
            int authResult = auth(buf);
            if (authResult > -1) {
                List<String> list = List.of(String.valueOf(authResult));
                ByteBuf bufOut = BufWorker.makeBufFromList(list, b);
                ctx.fireChannelRead(bufOut);
                // remove handler after auth was done
                ctx.pipeline().remove(this);
            }
        }
        List<String> list = List.of(String.valueOf(-1));
        ByteBuf bufOut = BufWorker.makeBufFromList(list, -1);
        ctx.writeAndFlush(bufOut);
    }

    private int auth(ByteBuf buf) {
        List<String> list = BufWorker.readFileListFromBuf(buf);
        String login = list.get(0);
        String pass = list.get(1);
        return authService.getUserID(login, pass);
    }
}

