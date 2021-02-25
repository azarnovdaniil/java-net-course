package ru.daniilazarnov.test;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import ru.daniilazarnov.test.db.DBConnect;
import ru.daniilazarnov.test.entity.User;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;

public class TestServerHandler extends ChannelInboundHandlerAdapter {
    private static final byte CMD_UPLOAD = (byte) 25;
    private static final byte CMD_START_UPLOAD = (byte) 35;
    private static final byte CMD_DOWNLOAD = (byte) 36;
    private static final byte CMD_LS = (byte) 45;

    private Set<User> users = new HashSet<>();
    private User user;


    private final String ROOT = "D:\\testDir\\Server\\";
    private String clientDir;

    private TransferState transferState = TransferState.READY;
    private ServerState state = ServerState.AUTH;

    private int nextLength;
    private long fileLength;
    private long receivedFileLength;
    private BufferedOutputStream out;

    private DBConnect service = new DBConnect();


// auth admin admin
// upl d:\testDir\Client\dm.zip

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Client connected");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg;
        while (buf.readableBytes() > 0){

            if (state == ServerState.AUTH){
                byte signal = buf.readByte();
                if (signal == (byte) 10){
                    String[] str = buf.toString(StandardCharsets.UTF_8).split("\\s");
                    String login = str[0];
                    String pass = str[1];
                    if (service.doAuth(login, pass, users, user)){
                        clientDir = ROOT + login + "\\";
                        File file = new File(clientDir);
                        if (!file.exists()){
                            file.mkdirs();
                        }
                        System.out.println(file.getAbsolutePath());
                        state = ServerState.IDLE;
                    } else {
                        System.out.println("Please try again...");
                    }
                }
            }

            if (state == ServerState.IDLE){
                byte signal = buf.readByte();
                if (signal == CMD_START_UPLOAD){
                    state = ServerState.TRANSFER;
                    receiveFile(buf);
                } else if (signal == CMD_LS){
                    System.out.println("ls");
                } else if (signal == CMD_DOWNLOAD){
                    System.out.println("download");
                }
            } else if (state == ServerState.TRANSFER){
                receiveFile(buf);
            }
        }
    }

    private void receiveFile(ByteBuf buf) throws IOException {
        while (buf.readableBytes() > 0) {
            try {
                if (transferState == TransferState.READY) {
                    if (buf.readByte() == CMD_UPLOAD) {
                        transferState = TransferState.NAME_LENGTH;
                        receivedFileLength = 0L;
                        System.out.println("STATE: Start file receiving");
                    } else {
                        System.out.println("ERROR: Invalid first byte - ");
                    }
                }

                if (transferState == TransferState.NAME_LENGTH) {
                    if (buf.readableBytes() >= (Integer.SIZE / Byte.SIZE)) {
                        System.out.println("STATE: Get filename length");
                        nextLength = buf.readInt();
                        transferState = TransferState.NAME;
                    }
                }
                if (transferState == TransferState.NAME) {
                    if (buf.readableBytes() >= nextLength) {
                        byte[] fileName = new byte[nextLength];
                        buf.readBytes(fileName);
                        System.out.println("STATE: Filename received - _" + new String(fileName, "UTF-8"));
                        out = new BufferedOutputStream(new FileOutputStream(clientDir + new String(fileName)));
                        transferState = TransferState.FILE_LENGTH;
                    }
                }

                if (transferState == TransferState.FILE_LENGTH) {
                    if (buf.readableBytes() >= (Long.SIZE / Byte.SIZE)) {
                        fileLength = buf.readLong();
                        System.out.println("STATE: File length received - " + fileLength);
                        transferState = TransferState.FILE;
                    }
                }

                if (transferState == TransferState.FILE) {
                    while (buf.readableBytes() > 0) {
                        out.write(buf.readByte());
                        receivedFileLength++;
                        if (fileLength == receivedFileLength) {
                            state = ServerState.IDLE;
                            transferState = TransferState.READY;
                            System.out.println("File received");
                            out.close();
                        }
                    }
                }
            } finally {
                if (buf.readableBytes() == 0) {
                    buf.release();
                }
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("Client disconnected");
        cause.printStackTrace();
        ctx.close();
    }
}
