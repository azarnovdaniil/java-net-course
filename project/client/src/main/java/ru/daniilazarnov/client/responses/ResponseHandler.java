package ru.daniilazarnov.client.responses;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import ru.daniilazarnov.client.responses.show.ShowResponseHandler;
import ru.daniilazarnov.common.OperationTypes;
import ru.daniilazarnov.common.handlers.Handler;
import ru.daniilazarnov.common.handlers.HandlerState;

public class ResponseHandler extends ChannelInboundHandlerAdapter {
    private HandlerState state = HandlerState.IDLE;
    private Handler handler;

    public ResponseHandler() {
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = ((ByteBuf) msg);

        while (buf.readableBytes() > 0) {
            if (state == HandlerState.IDLE) {
                handler = readOperationType(ctx, buf);
                if (handler != null) {
                    state = HandlerState.PROCESS;
                }
            }
            if (state == HandlerState.PROCESS) {
                handler.setBuffer(buf);
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

    private Handler readOperationType(ChannelHandlerContext ctx, ByteBuf buf) {
        byte read = buf.readByte();
        if (read == OperationTypes.DOWNLOAD.getCode()) {
            return null;
        } else if (read == OperationTypes.SHOW.getCode()) {
            return new ShowResponseHandler();
        } else {
            System.out.println("ERROR: Invalid first byte - " + read);
            return null;
        }
    }
}
