package ru.gb.putilin.cloudstorage.server.handlers;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import ru.gb.putilin.cloudstorage.common.OperationType;
import ru.gb.putilin.cloudstorage.server.handlers.download.DownloadHandler;
import ru.gb.putilin.cloudstorage.server.handlers.show.ShowHandler;
import ru.gb.putilin.cloudstorage.server.handlers.upload.UploadHandler;


public class InputHandler extends ChannelInboundHandlerAdapter {

    private HandlerState state = HandlerState.IDLE;
    private FileHandler handler;

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
        public void exceptionCaught (ChannelHandlerContext ctx, Throwable cause) throws Exception {
            cause.printStackTrace();
            ctx.close();
        }

        private FileHandler readOperationType (ChannelHandlerContext ctx, ByteBuf buf){
            byte read = buf.readByte();
            if (read == OperationType.DOWNLOAD.getCode()) {
                System.out.println("DOWNLOAD operation");
                return new DownloadHandler(ctx);
            } else if (read == OperationType.UPLOAD.getCode()) {
                System.out.println("UPLOAD operation");
                return new UploadHandler(ctx);
            } else if (read == OperationType.SHOW.getCode()) {
                System.out.println("SHOW operation");
                return new ShowHandler(ctx);
            } else {
                System.out.println("ERROR: Invalid first byte - " + read);
                return null;
            }
        }

    }
