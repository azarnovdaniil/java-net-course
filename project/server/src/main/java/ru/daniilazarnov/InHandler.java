package ru.daniilazarnov;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;


public class InHandler extends ChannelInboundHandlerAdapter {

    private String serverDir = "project/server/serverFiles";
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
            if (readed == (byte) 2) {
                System.out.println("Get byte = " + readed);
                currentState = State.NAME_LENGTH;
                receivedFileLength = 0L;
                System.out.println("STATE: Start file receiving");

                // на выгрузку файла
            } else if (readed == (byte) 3) {
                System.out.println("Get byte = " + readed);
                currentState = State.NAME_LENGTH_DOWNLOAD;
                System.out.println("STATE: Start file downloading");

                // на получение списка файлов на сервере
            } else if (readed == (byte) 1) {
                System.out.println("Get byte = " + readed);
                buf.release();                //освобождение ByteBuf
                ctx.fireChannelRead("ls");
            } else {
                buf.release();
                System.out.println("ERROR: Invalid first byte - " + readed);
            }
        }

        // на загрузку файла
        if (currentState == State.NAME_LENGTH) {
            if (buf.readableBytes() >= 4) {
                nextLength = buf.readInt();
                System.out.println("STATE: Get filename length - " + nextLength + " bytes");
                currentState = State.NAME;
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
            }
        }

        // на загрузку файла
        if (currentState == State.FILE_LENGTH) {
            if (buf.readableBytes() >= 8) {
                fileLength = buf.readLong();
                System.out.println("STATE: File length received - " + fileLength + " bytes");
                currentState = State.FILE;
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
                    int i = 2;
                    ctx.write(i);
                    ctx.flush();
                    break;
                }
            }
        }

        // на выгрузку файла
        if (currentState == State.NAME_LENGTH_DOWNLOAD) {
            if (buf.readableBytes() >= 4) {
                nextLength = buf.readInt();
                System.out.println("STATE: Get downloading filename length - " + nextLength + " bytes");
                currentState = State.NAME_DOWNLOAD;
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
            buf.release();                           //освобождение ByteBuf
            System.out.println("buf.release");
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
//            ByteBuf buf = ctx.alloc().buffer(arr.length); // alloc() - ссылка на базовый Аллокатор, который выделяет память для буфера
//            buf.writeBytes(arr);
//            ctx.writeAndFlush(buf);

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {      //не забывать писать для всех хендлеров
        cause.printStackTrace();
        ctx.close();
    }
}
