package ru.daniilazarnov;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.apache.log4j.Logger;

import java.nio.file.Path;

public class FileReceiveHandler extends ChannelInboundHandlerAdapter {
    private static final Logger log = Logger.getLogger(FileReceiveHandler.class);
    private  final String user ="server";
    private State currentState = State.IDLE;
    public static final String HOME_FOLDER_PATH = "project/server/cloud_storage/user1";


    @Override
    public void channelActive(ChannelHandlerContext ctx){
        System.out.println("Клиент подключился");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        System.err.println("Клиент отключился");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = ((ByteBuf) msg);

        if (currentState == State.IDLE) {
            byte readed = buf.readByte();
            if (readed == (byte) 25) {
//                currentState = State.NAME_LENGTH;
                System.out.println("STATE: Start file receiving");
                buf.resetReaderIndex();
                ReceivingFiles.fileReceive(buf, user);
                buf.clear();
            } else if (readed == (byte) 1) {
//                buf.resetReaderIndex();
             String fileName =  UtilMethod.receiveAndEncodeString(buf);
                System.out.println("fileName ".toUpperCase() + fileName);

                System.out.println("STATE: Start file download");
                FileSender.sendFile(Path.of(HOME_FOLDER_PATH, fileName), ctx.channel(), UtilMethod.getChannelFutureListener("Файл успешно передан"));
                buf.clear();
                return;
            } else {
                System.out.println("(class FileReceiveHandler) ERROR: Invalid first byte - " + readed);
                buf.resetReaderIndex();
//                ctx.fireChannelRead(buf);
//                return;
            }
        }
    }





    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause){
        System.err.println(cause.getMessage());
        cause.printStackTrace();
        log.error(cause);
//        ctx.close();
    }
}
