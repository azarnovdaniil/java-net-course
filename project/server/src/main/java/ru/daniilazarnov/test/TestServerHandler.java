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
    private static final String ROOT = "D:\\testDir\\Server\\"; //временный путь
//    private static final String ROOT = new File("").getAbsolutePath() + "\\FileStorage\\";
    private Set<User> users;
    private User user;
    private String clientDir = "";
    private ServerState state = ServerState.AUTH;
    private DBConnect service = new DBConnect();
    private Commands command;

    public TestServerHandler(Set<User> users) {
        this.users = users;
    }

/*
 auth admin admin
 auth nick1 pass1
 upl d:\testDir\Client\mu.zip
*/

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Client connected");
        command = new Commands();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg;
        while (buf.readableBytes() > 0){
            if (state == ServerState.AUTH){
                byte signal = buf.readByte();
                if (signal == Signals.AUTH.get()){
                    String[] str = buf.toString(StandardCharsets.UTF_8).split("\\s");
                    String login = str[0];
                    String pass = str[1];

                    user = service.doAuth(login, pass);
                    if (user != null){
                        if (users.stream().noneMatch(u -> u.getLogin().equals(login))){
                            users.add(user);
                            System.out.println(user.getLogin() + " is logged in");

                            clientDir = ROOT + login + "\\";
                            File file = new File(clientDir);
                            if (!file.exists()){
                                file.mkdirs();
                            }
                            System.out.println(file.getAbsolutePath());

                            state = ServerState.IDLE;
                        } else {
                            System.out.println("This user already logged in");
                        }
                    }

                } else if (signal == Signals.REG.get()){
                    String[] str = buf.toString(StandardCharsets.UTF_8).split("\\s");
                    String login = str[0];
                    String pass = str[1];
                    if (service.doReg(login, pass)){
                        System.out.println(login + " registered");
                    } else {
                        System.out.println(login + " user already exists");
                    }
                }
            }

            if (state == ServerState.IDLE){
                byte signal = buf.readByte();
                if (signal == Signals.START_UPLOAD.get()){
                    state = ServerState.TRANSFER;
                    command.receiveFile(buf, clientDir);
                } else if (signal == Signals.LS.get()){
                    command.listFiles(clientDir, ctx, future -> {
                        if (!future.isSuccess()) {
                            System.out.println("SWW during sending LS");
                            future.cause().printStackTrace();
                        }
                        if (future.isSuccess()) {
                            System.out.println("List of files sent successfully");
                        }
                    });
                } else if (signal == Signals.RM.get()){
                    String fileName = buf.toString(StandardCharsets.UTF_8);
                    String path = clientDir + fileName;
                    command.removeFile(path);
                } else if (signal == Signals.DOWNLOAD.get()){
                    System.out.println("download");
                }
            } else if (state == ServerState.TRANSFER){
                if (command.receiveFile(buf, clientDir)) {
                    state = ServerState.IDLE;
                }
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.printf("[%s] disconnected", user.getLogin());
        users.remove(user);
        cause.printStackTrace();
        ctx.close();
    }
}
