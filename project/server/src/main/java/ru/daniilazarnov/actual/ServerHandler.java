package ru.daniilazarnov.actual;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import ru.daniilazarnov.actual.db.DBConnect;
import ru.daniilazarnov.actual.entity.User;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.Set;

public class ServerHandler extends ChannelInboundHandlerAdapter {
    private static final String ROOT = "D:\\testDir\\Server\\"; //временный путь
//    private static final String ROOT = new File("").getAbsolutePath() + "\\FileStorage\\";
    private Set<User> users;
    private User user;
    private String clientDir = "";
    private ServerState state = ServerState.AUTH;
    private DBConnect service = new DBConnect();
    private Commands command;
    private RequestController controller;

    public ServerHandler(Set<User> users) {
        this.users = users;
    }

/*
 auth admin admin
 auth nick1 pass1
 upl d:\testDir\Client\mu.zip
 down mu.zip d:\testDir\Client\1
*/

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Client connected");
        command = new Commands();
        controller = new RequestController(ctx.channel());
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
                            command.sendMessage("Авторизация прошла упешно. Добро пожаловать " + login + "!", ctx);
                            state = ServerState.IDLE;
                        } else {
                            command.sendMessage("Данный пользователь уже авторизован, проверьте правильность логина и пароля...", ctx);
                        }
                    }

                } else if (signal == Signals.REG.get()){
                    String[] str = buf.toString(StandardCharsets.UTF_8).split("\\s");
                    String login = str[0];
                    String pass = str[1];
                    if (service.doReg(login, pass)){
                        System.out.println(login + " registered");
                        command.sendMessage("Регистрация прошла успешно. Пожалуйста пройдите авторизацию", ctx);
                    } else {
                        System.out.println(login + " user already exists");
                        command.sendMessage("Данный логин уже зарегистрирован в системе.", ctx);
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
                    if (command.removeFile(path)) {
                        command.sendMessage(String.format("Файл [%s] успешно удален", fileName), ctx);
                    } else {
                        command.sendMessage("При удалении файла возникла ошибка", ctx);
                    }
                } else if (signal == Signals.DOWNLOAD.get()){
                    String[] str = buf.toString(StandardCharsets.UTF_8).split("\\s");
                    String filename = str[0];
                    String path = str[1];
                    File file = new File(clientDir + filename);
                    if (file.exists() && !file.isDirectory()){
                        command.sendMessage("Производится скачивание, пожалуйста подождите...", ctx);
                        controller.command("cd " + path + "\\");
                        controller.command("upl " + file.getAbsolutePath());
                    } else {
                        command.sendMessage("Ошибка скачивания, проверьте правильность имени файла", ctx);
                    }
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
