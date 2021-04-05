package ru.daniilazarnov;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;


public class ServerHandler extends ChannelInboundHandlerAdapter {

    private final ServerFileProtocol serverProtocol = new ServerFileProtocol();
    private Path path;
    private long fileReqLength = 0;
    private long loadedLength = 0;
    private Commands command = Commands.EMPTY;
    private State state = State.START_TYPE;
    private String fileName;
    private int reqNameLength = 0;
    private BufferedOutputStream bos;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        serverProtocol.setCtx(ctx);
        ByteBuf buf = (ByteBuf) msg;

        switch (state) {
            case START_TYPE:
                command = command.getCommand(buf.readByte());
                state = State.GET_COMMAND;
                break;
            case GET_COMMAND:
                getCommandFromBytes(buf.readByte());
                break;
            case NAME_LENGTH:
                getFileNameLength(buf);
                break;
            case NAME:
                getFileName(buf);
                break;
            case CREATE_DIR:
                serverProtocol.createDir(path);
                state = State.SEND_LIST;
                break;
            case MOVE_FORWARD:
                serverProtocol.moveForward(fileName);
                state = State.SEND_LIST;
                break;
            case DELETE_FILE:
                serverProtocol.deleteFile(path);
                state = State.SEND_LIST;
                break;
            case SEND_FILE:
                serverProtocol.writeFile(fileName, path);
                buf.release();
                state = State.SEND_LIST;
                break;
            case MOVE_BACK:
                serverProtocol.moveBack();
                state = State.GET_COMMAND;
                break;
            case SEND_LIST:
                serverProtocol.sendFilesList();
                state = State.GET_COMMAND;
                break;
            case FILE_LENGTH:
                getFileLength(buf);
                break;
            case GET_FILE:
                getFile(buf);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + state);
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        System.out.println("Client connected...");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        System.out.println("Client disconnected...");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

    public void getCommandFromBytes(byte b) {
        command = command.getCommand(b);
        switch (command) {
            case DOWNLOAD:
            case UPLOAD:
            case CREATE:
            case FORWARD:
            case DELETE:
                state = State.NAME_LENGTH;
                break;
            case BACK:
                state = State.MOVE_BACK;
                break;
            case HELP:
                state = State.SEND_LIST;
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + command);
        }
    }

    public void getFileNameLength(ByteBuf buf) {
        if (buf.readableBytes() < Integer.SIZE / Byte.SIZE) {
            return;
        }
        reqNameLength = buf.readInt();
        state = State.NAME;
    }

    public void getFileName(ByteBuf buf) {
        if (buf.readableBytes() < reqNameLength) {
            return;
        }
        byte[] fileNameArr = new byte[reqNameLength];
        buf.readBytes(fileNameArr);
        fileName = new String(fileNameArr);
        path = Paths.get(serverProtocol.getServerPath() + fileName);
        switch (command) {
            case DOWNLOAD:
                state = State.SEND_FILE;
                break;
            case UPLOAD:
                state = State.FILE_LENGTH;
                break;
            case DELETE:
                state = State.DELETE_FILE;
                break;
            case CREATE:
                state = State.CREATE_DIR;
                break;
            case FORWARD:
                state = State.MOVE_FORWARD;
                break;
            default:
                state = State.GET_COMMAND;
                break;
        }
    }

    public void getFileLength(ByteBuf buf) throws FileNotFoundException {
        if (buf.readableBytes() < Long.SIZE / Byte.SIZE) {
            return;
        }
        fileReqLength = buf.readLong();
        loadedLength = 0;
        serverProtocol.createFile(path);
        bos = new BufferedOutputStream(new FileOutputStream(path.toString(), true));
        state = State.GET_FILE;
    }

    public void getFile(ByteBuf buf) throws IOException {
        while (buf.readableBytes() > 0 && loadedLength < fileReqLength) {
            bos.write(buf.readByte());
            loadedLength++;
        }
        if (loadedLength < fileReqLength) {
            return;
        }
        bos.flush();
        buf.release();
        bos.close();
        state = State.SEND_LIST;
    }


}
