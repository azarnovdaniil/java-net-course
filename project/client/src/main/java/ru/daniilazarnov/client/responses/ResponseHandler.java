package ru.daniilazarnov.client.responses;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import ru.daniilazarnov.client.responses.download.DownloadResponseHandler;
import ru.daniilazarnov.client.responses.message.MessageResponseHandler;
import ru.daniilazarnov.client.responses.show.ShowResponseHandler;
import ru.daniilazarnov.common.commands.Commands;
import ru.daniilazarnov.common.handlers.Handler;
import ru.daniilazarnov.common.handlers.HandlerState;

public class ResponseHandler extends ChannelInboundHandlerAdapter {
    private HandlerState state = HandlerState.IDLE;
    private Handler handler;
    private String downloadsPath;

    public ResponseHandler(String downloadsPath) {
        this.downloadsPath = downloadsPath;
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
        if (read == Commands.UPLOAD.getCode()) {
            return new DownloadResponseHandler(downloadsPath, buf);
        }
        if (read == Commands.SHOW.getCode()) {
            return new ShowResponseHandler(buf);
        }
        if (read == Commands.MESSAGE.getCode()) {
            return new MessageResponseHandler(buf);
        }

        System.out.println("ERROR: Invalid first byte - " + read);
        return null;

    }
}
