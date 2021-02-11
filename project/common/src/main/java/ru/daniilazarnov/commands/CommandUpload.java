package ru.daniilazarnov.commands;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.*;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

public class CommandUpload implements Command {

    private State currentState = State.IDLE;
    private int nextLength;
    private long fileLength;
    private long receivedFileLength;
    private BufferedOutputStream out;
    private File serverFolder = new File(FileSystemView.getFileSystemView().getHomeDirectory() + File.separator + "Server");

    @Override
    public void send(ChannelHandlerContext ctx, byte signal) {
        try {
            JFileChooser chooser = new JFileChooser();
            chooser.setCurrentDirectory(FileSystemView.getFileSystemView().getHomeDirectory());
            int send = chooser.showDialog(null, "Send");
            if (send == JFileChooser.APPROVE_OPTION) {
                Path path = Paths.get(chooser.getSelectedFile().getPath());

                ByteBuf buf = null;
                buf = ByteBufAllocator.DEFAULT.directBuffer(1);
                buf.writeByte(signal);
                ctx.writeAndFlush(buf);

                byte[] filenameBytes = path.getFileName().toString().getBytes(StandardCharsets.UTF_8);
                buf = ByteBufAllocator.DEFAULT.directBuffer(4);
                buf.writeInt(filenameBytes.length);
                ctx.writeAndFlush(buf);

                buf = ByteBufAllocator.DEFAULT.directBuffer(filenameBytes.length);
                buf.writeBytes(filenameBytes);
                ctx.writeAndFlush(buf);

                buf = ByteBufAllocator.DEFAULT.directBuffer(8);
                buf.writeLong(Files.size(path));
                ctx.writeAndFlush(buf);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void response(ChannelHandlerContext ctx, ByteBuf buf) {

        try {
            while (buf.readableBytes() > 0) {
                if (currentState == State.IDLE) {
                    currentState = State.NAME_LENGTH;
                    receivedFileLength = 0L;
                    System.out.println("STATE: Start file receiving");
                }

                if (currentState == State.NAME_LENGTH) {
                    if (buf.readableBytes() >= 4) {
                        System.out.println("STATE: Get filename length");
                        nextLength = buf.readInt();
                        currentState = State.NAME;
                    }
                }
                if (currentState == State.NAME) {
                    if (buf.readableBytes() >= nextLength) {
                        byte[] fileName = new byte[nextLength];
                        buf.readBytes(fileName);
                        System.out.println("STATE: Filename received - " + new String(fileName, "UTF-8"));
                        out = new BufferedOutputStream(new FileOutputStream(serverFolder + File.separator + new String(fileName)));
                        currentState = State.FILE_LENGTH;
                    }
                }

                if (currentState == State.FILE_LENGTH) {
                    if (buf.readableBytes() >= 8) {
                        fileLength = buf.readLong();
                        System.out.println("STATE: File length received - " + fileLength);
                        currentState = State.FILE;
                    }
                }

                if (currentState == State.FILE) {
                    while (buf.readableBytes() > 0) {
                        out.write(buf.readByte());
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
            if (buf.readableBytes() == 0) {
                buf.release();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}