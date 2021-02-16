package ru.daniilazarnov;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;


public class InHandler extends ChannelInboundHandlerAdapter {

    private static final byte LS_BYTE = 1;
    private static final byte UPLOAD_BYTE = 2;
    private static final byte DOWNLOAD_BYTE = 3;
    private static final int INT_BYTES = 4;
    private static final int LONG_BYTES = 8;
    private final String serverDir = "project/server/serverFiles";
    private State currentState = State.IDLE;
    private int nextLength;
    private long fileLength;
    private long receivedFileLength;
    private BufferedOutputStream out;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        ByteBuf buf = (ByteBuf) msg;

        if (currentState == State.IDLE) {
            byte readed = buf.readByte();

            // на загрузку файла
            if (readed == UPLOAD_BYTE) {
                System.out.println("Get byte = " + readed);
                currentState = State.NAME_LENGTH;
                receivedFileLength = 0L;
                System.out.println("STATE: Start file receiving");
                ctx.writeAndFlush(UPLOAD_BYTE);

                // на выгрузку файла
            } else if (readed == DOWNLOAD_BYTE) {
                System.out.println("Get byte = " + readed);
                currentState = State.NAME_LENGTH_DOWNLOAD;
                System.out.println("STATE: Start file downloading");
                ctx.writeAndFlush(DOWNLOAD_BYTE);

                // на получение списка файлов на сервере
            } else if (readed == LS_BYTE) {
                System.out.println("Get byte = " + readed);
                ctx.fireChannelRead("ls");
            } else {
                System.out.println("ERROR: Invalid first byte - " + readed);
                buf.release();
            }
        }

        // на загрузку файла
        if (currentState == State.NAME_LENGTH) {
            if (buf.readableBytes() >= INT_BYTES) {
                nextLength = buf.readInt();
                System.out.println("STATE: Get filename length - " + nextLength + " bytes");
                currentState = State.NAME;
                ctx.writeAndFlush(UPLOAD_BYTE);
            }
        }

        // на загрузку файла
        if (currentState == State.NAME) {
            if (buf.readableBytes() >= nextLength) {
                byte[] fileName = new byte[nextLength];
                buf.readBytes(fileName);
                System.out.println("STATE: Filename received - " + new String(fileName, "UTF-8"));
                out = new BufferedOutputStream(new FileOutputStream(serverDir + "/" + new String(fileName)));
                currentState = State.FILE_LENGTH;
                ctx.writeAndFlush(UPLOAD_BYTE);
            }
        }

        // на загрузку файла
        if (currentState == State.FILE_LENGTH) {
            if (buf.readableBytes() >= LONG_BYTES) {
                fileLength = buf.readLong();
                System.out.println("STATE: File length received - " + fileLength + " bytes");
                currentState = State.FILE;
                ctx.writeAndFlush(UPLOAD_BYTE);
            }
        }

        // на загрузку файла
        if (currentState == State.FILE) {
            while (buf.readableBytes() > 0) {
                out.write(buf.readByte());
                receivedFileLength++;
                System.out.println(receivedFileLength);
                if (fileLength == receivedFileLength) {
                    currentState = State.IDLE;
                    System.out.println("File received");
                    out.close();
                    ctx.writeAndFlush(UPLOAD_BYTE);
                    break;
                }
            }
        }

        // на выгрузку файла
        if (currentState == State.NAME_LENGTH_DOWNLOAD) {
            if (buf.readableBytes() >= INT_BYTES) {
                nextLength = buf.readInt();
                System.out.println("STATE: Get downloading filename length - " + nextLength + " bytes");
                currentState = State.NAME_DOWNLOAD;
                ctx.writeAndFlush(DOWNLOAD_BYTE);
            }
        }

        // на выгрузку файла
        if (currentState == State.NAME_DOWNLOAD) {
            if (buf.readableBytes() >= nextLength) {
                byte[] fileName = new byte[nextLength];
                buf.readBytes(fileName);
                String name = new String(fileName, "UTF-8");
                System.out.println("STATE: Downloading filename received - " + name);
                ctx.write("download: " + name);
                ctx.flush();
                currentState = State.IDLE;
            }
        }

        if (buf.readableBytes() == 0) {
            System.out.println("buf.release");
            buf.release();                           //освобождение ByteBuf
        }
    }

//        System.out.println(readed);
//        byte[] ls = new byte[2];
//        buf.readBytes(ls);
//        for (int i = 0; i < 2; i++) {
//            System.out.println(ls[i]);
//        }

//        byte readed = buf.readByte();
//        if(readed == (byte) 2) {
//            buf.release();                //освобождение ByteBuf
//            ctx.fireChannelRead("ls");

    //Вариант для приходящего сигнального байта и команды в виде String
//            if(readed == (byte) 2) {
//                byte[] ls = new byte[2];
//                buf.readBytes(ls);
//                String s = new String(ls, "UTF-8");
//                System.out.println(s);
//                buf.release();                //освобождение ByteBuf
//                ctx.fireChannelRead(s);

    //Исходный вариант для приходящих String
//            String str = (String) msg;
//            byte[] arr = (str).getBytes();
//            ByteBuf buf = ctx.alloc().buffer(arr.length);
//            buf.writeBytes(arr);
//            ctx.writeAndFlush(buf);


    //не забывать писать для всех хендлеров
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
