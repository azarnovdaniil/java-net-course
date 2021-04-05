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

public class ClientHandler extends ChannelInboundHandlerAdapter {

    private Path path = null;
    private long fileReqLength = 0;
    private long loadedLength = 0;
    private Commands command = Commands.EMPTY;
    private State state = State.GET_COMMAND;
    private int fileNameLength = 0;
    private BufferedOutputStream bos;
    private final Network network = new Network();
    private final ClientFileProtocol clientFileProtocol = new ClientFileProtocol(network);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg;
        System.out.println(buf.readByte());

        switch (state) {
            case GET_COMMAND:
                getCommandFromBytes(buf.readByte());
                break;
            case NAME_LENGTH:
                getFileNameLength(buf);
                break;
            case NAME:
                getFileName(buf);
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
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

    public void getCommandFromBytes(byte b) {
        command = command.getCommand(b);
        if (command == Commands.HELP){
            command.helpInfo();
            state = State.GET_COMMAND;
        } else if (command == Commands.FILE) {
            state = State.NAME_LENGTH;
        }
    }

    public void getFileNameLength(ByteBuf buf) {
        if (buf.readableBytes() < 4) return;
        fileNameLength = buf.readInt();
        state = State.NAME;
    }

    public void getFileName(ByteBuf buf) {
        if (buf.readableBytes() < fileNameLength) return;
        byte[] fileNameArr = new byte[fileNameLength];
        buf.readBytes(fileNameArr);
        String fileName = new String(fileNameArr);
        if (command == Commands.HELP) {
            state = State.GET_COMMAND;
        } else if (command == Commands.FILE) {
            path = Paths.get("project/client/" + fileName);
            state = State.FILE_LENGTH;
        }
    }

    public void getFileLength(ByteBuf buf) throws FileNotFoundException {
        if (buf.readableBytes() < 8) return;
        fileReqLength = buf.readLong();
        loadedLength = 0;
        clientFileProtocol.createFile(path);
        bos = new BufferedOutputStream(new FileOutputStream(path.toString(), true));
        state = State.GET_FILE;
    }

    public void getFile(ByteBuf buf) throws IOException {
        while (buf.readableBytes() > 0 && loadedLength < fileReqLength) {
            bos.write(buf.readByte());
            loadedLength++;
        }
        if (loadedLength < fileReqLength) return;
        bos.flush();
        buf.release();
        bos.close();
        state = State.GET_COMMAND;
    }
}