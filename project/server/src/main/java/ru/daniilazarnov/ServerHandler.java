package ru.daniilazarnov;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

import java.util.Date;


public class ServerHandler extends ChannelInboundHandlerAdapter {
    public static final byte MAGIC_BYTE = (byte) 25;
    private State currentState = State.IDLE;
    private int nextLength;
    private long fileLength;
    private long receivedFileLength;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Время: " + new Date());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//        ByteBuf buf = ((ByteBuf) msg);
//        if (currentState == State.IDLE) {
//            byte readed = buf.readByte();
//            if (readed == MAGIC_BYTE) {
//                currentState = State.NAME_LENGTH;
//                receivedFileLength = 0L;
//                System.out.println("STATE: Start file receiving");
//            } else {
//                System.out.println(buf.toString(CharsetUtil.UTF_8));
//            }
//        }
        ByteBuf in = (ByteBuf) msg;
        try {
            while (in.isReadable()) {
                System.out.print((char) in.readByte());
            }
        } finally {
            in.release();
        }
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
