package ru.daniilazarnov;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;

public class Handler extends ChannelInboundHandlerAdapter {
    public enum State {
        IDLE, NAME_LENGTH, NAME, FILE_LENGTH, FILE
    }

    public State getCurrentState() {
        return currentState;
    }

    private State currentState = State.IDLE;
    private int nextLength;
    private long fileLength;
    private long receivedFileLength;
    private BufferedOutputStream out;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buff = (ByteBuf)msg;
        String s = buff.toString(StandardCharsets.UTF_8);
        if (buff.getByte(0)==Command.DOWNLOAD.getSignal() || currentState==State.FILE){
        while (buff.readableBytes() > 0) {
            if (currentState == State.IDLE) {
                    currentState = State.NAME_LENGTH;
                    receivedFileLength = 0L;
                    System.out.println("STATE: Start file receiving");
            }


            if (currentState == State.NAME_LENGTH) {
                if (buff.readableBytes() >= 4) {
                    System.out.println("STATE: Get filename length");
                    nextLength = buff.readInt();
                    currentState = State.NAME;
                }
            }

            if (currentState == State.NAME) {
                if (buff.readableBytes() >= nextLength) {
                    byte[] fileName = new byte[nextLength];
                    buff.readBytes(fileName);
                    System.out.println("STATE: Filename received - " + new String(fileName, "UTF-8"));
                    out = new BufferedOutputStream(new FileOutputStream(new String(fileName)));
                    currentState = State.FILE_LENGTH;
                }
            }

            if (currentState == State.FILE_LENGTH) {
                if (buff.readableBytes() >= 8) {
                    fileLength = buff.readLong();
                    System.out.println("STATE: File length received - " + fileLength);
                    currentState = State.FILE;
                }
            }

            if (currentState == State.FILE) {
                while (buff.readableBytes() > 0) {
                    out.write(buff.readByte());
                    receivedFileLength++;
                    if (fileLength == receivedFileLength) {
                        currentState = State.IDLE;
                        System.out.println("File received");
                        out.close();
                        break;
                    }
                }
            }
        }
        }
        else{
        System.out.println(s);}
        if (buff.readableBytes() == 0) {
            buff.release();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
    }
}
