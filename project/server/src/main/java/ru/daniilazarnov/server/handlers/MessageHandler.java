package ru.daniilazarnov.server.handlers;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import ru.daniilazarnov.common.handlers.Handler;
import ru.daniilazarnov.common.handlers.HandlerState;
import ru.daniilazarnov.server.auth.AuthService;
import ru.daniilazarnov.server.operations.ServerOperationsFactory;


public class MessageHandler extends ChannelInboundHandlerAdapter {

    private HandlerState state = HandlerState.IDLE;
    private Handler handler;
    private String root;
    private AuthService authService;

    public MessageHandler(String root, AuthService authService) {
        this.root = root;
        this.authService = authService;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = ((ByteBuf) msg);

        while (buf.readableBytes() > 0) {
            if (state == HandlerState.IDLE) {
                handler = new ServerOperationsFactory(authService).
                        createOperation(buf.readByte(), ctx.channel(), root, buf);
                if (handler != null) {
                    state = HandlerState.PROCESS;
                }
            }
            if (state == HandlerState.PROCESS) {
                handler.handle();
                if (handler.isComplete()) {
                    state = HandlerState.IDLE;
                }
            }
            if (buf.readableBytes() == 0) {
                buf.release();
            }

        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }


    private boolean checkAuth(ChannelHandlerContext ctx) {
        authService.checkSession(ctx.channel().id().toString());
        return false;
    }

}
