package ru.daniilazarnov.server.handlers;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import ru.daniilazarnov.common.OperationTypes;
import ru.daniilazarnov.common.handlers.Handler;
import ru.daniilazarnov.common.handlers.HandlerState;
import ru.daniilazarnov.server.handlers.download.DownloadHandler;
import ru.daniilazarnov.server.handlers.show.ShowHandler;
import ru.daniilazarnov.server.handlers.upload.UploadHandler;


public class InputHandler extends ChannelInboundHandlerAdapter {

    private HandlerState state = HandlerState.IDLE;
    private Handler handler;
    private String root;

    public InputHandler(String root) {
        this.root = root;
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
            System.out.println("DOWNLOAD operation");
            return new DownloadHandler(ctx, root);
        } else if (read == OperationTypes.UPLOAD.getCode()) {
            System.out.println("UPLOAD operation");
            return new UploadHandler(ctx, root);
        } else if (read == OperationTypes.SHOW.getCode()) {
            System.out.println("SHOW operation");
            return new ShowHandler(ctx, root);
        } else {
            System.out.println("ERROR: Invalid first byte - " + read);
            return null;
        }
    }

}
