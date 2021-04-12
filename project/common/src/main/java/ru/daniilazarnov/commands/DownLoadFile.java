package ru.daniilazarnov.commands;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.*;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class DownLoadFile extends ChannelInboundHandlerAdapter {
    public final byte DOWNLOAD = (byte) 25;
    public final byte UPLOAD = (byte) 24;
    private State currentState = State.IDLE;
    private int nextLength;
    private String fileName;
    private long fileLength;
    private long receivedFileLength;
    private BufferedOutputStream out;
    private FileOutputStream fos;
    private   Path path = Paths.get("C:\\Users\\Stas\\Desktop\\Java.NET\\java-net-course\\project\\server\\src\\main\\java\\ru");



    public  void downloadRequest(Path path, Channel channel, ChannelFutureListener finishListener) throws IOException {
       //Запрос на скачивание
        FileRegion region = new DefaultFileRegion(path.toFile(), 0, Files.size(path));
        ByteBuf buf;
        buf = ByteBufAllocator.DEFAULT.directBuffer(1);
        buf.writeByte(UPLOAD);
        channel.writeAndFlush(buf);

        byte[] filenameBytes = path.getFileName().toString().getBytes(StandardCharsets.UTF_8);
        buf = ByteBufAllocator.DEFAULT.directBuffer(Integer.SIZE / Byte.SIZE);
        buf.writeInt(filenameBytes.length);
        channel.writeAndFlush(buf);

        buf = ByteBufAllocator.DEFAULT.directBuffer(filenameBytes.length);
        buf.writeBytes(filenameBytes);
        channel.writeAndFlush(buf);


        buf = ByteBufAllocator.DEFAULT.directBuffer(Long.SIZE / Byte.SIZE);
        buf.writeLong(Files.size(path));
        channel.writeAndFlush(buf);

        ChannelFuture transferOperationFuture = channel.writeAndFlush(region);
        if (finishListener != null) {
            transferOperationFuture.addListener(finishListener);
        }

    }


    public  void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = ((ByteBuf) msg);
        while (buf.readableBytes() > 0) {
            //Проверка ключа операции
            if (currentState == State.IDLE) {
                byte readed = buf.readByte();
                if (readed == DOWNLOAD) {
                    currentState = State.NAME_LENGTH;
                    receivedFileLength = 0L;
                    System.out.println("STATE: Start file receiving");
                } else {
                    System.out.println("ERROR: Invalid first byte - " + readed);
                }
                if (readed == UPLOAD) {
                    System.out.println("Команда читается сервером");
                    break;
                }
            }

            //Длинна названия файла
            if (currentState == State.NAME_LENGTH) {
                if (buf.readableBytes() >= (Integer.SIZE / Byte.SIZE)) {
                    System.out.println("STATE: Get filename length " + nextLength);
                    nextLength = buf.readInt();
                    currentState = State.NAME;
                }
            }

            //Имя файла
            if (currentState == State.NAME) {
                if (buf.readableBytes() >= nextLength) {
                    byte[] fName = new byte[nextLength];
                    buf.readBytes(fName);
                    System.out.println("STATE: Filename received - _" + new String(fName, "UTF-8"));
                    // out = new BufferedOutputStream(new FileOutputStream("_" + new String(fName)));
                    currentState = State.FILE_LENGTH;
                    fileName = new String(fName, "UTF-8");
                    fos = new FileOutputStream(path + fileName);
                    // fos = new FileOutputStream(f);
                }

            }
            //Длинна файла
            if (currentState == State.FILE_LENGTH) {
                if (buf.readableBytes() >= (Long.SIZE / Byte.SIZE)) {
                    fileLength = buf.readLong();
                    System.out.println("STATE: File length received - " + fileLength);
                    currentState = State.FILE;
                }
            }
            //Содержание файла
            if (currentState == State.FILE) {
                int count;
                while (buf.readableBytes() > 0) {
                    //out.write(buf.readByte());
                    byte[] b = new byte[(int) fileLength];
                    buf.readBytes(b);

                    fos.write(b, 0, (int) fileLength);
                    receivedFileLength++;
                    if (fileLength == receivedFileLength) {
                        currentState = State.IDLE;
                        System.out.println("File received");
                        fos.close();
                        out.close();
                        break;
                    }
                }
            }
        }
        if (buf.readableBytes() == 0) {
            buf.release();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
