package ru.sviridovaleksey.newclientconnection;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import ru.sviridovaleksey.Command;
import ru.sviridovaleksey.TypeCommand;
import ru.sviridovaleksey.clientHandler.BaseAuthService;
import ru.sviridovaleksey.workwithfiles.WorkWithFile;
import java.io.File;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerInHandler extends ChannelInboundHandlerAdapter {

    private Boolean isChanelReg = false;
    private String login;
    private WhatDo whatDo;
    private final BaseAuthService baseAuthService;
    private static final Logger LOGGER = Logger.getLogger(ServerInHandler.class.getName());
    private final Handler fileHandler;
    private final String defaultAddress;


    public ServerInHandler(Handler fileHandler, String defaultAddress) {
        this.fileHandler = fileHandler;
        LOGGER.addHandler(fileHandler);
        this.defaultAddress = new File(new File(defaultAddress).getParent()).getParent();
        baseAuthService = new BaseAuthService(this.defaultAddress);
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) {
        LOGGER.log(Level.INFO, "Подключился новый пользователь");
        ctx.writeAndFlush(Command.ping());
        MessageForClient messageForClient = new MessageForClient(ctx);
        WorkWithFile workWithFile = new WorkWithFile(fileHandler);
        this.whatDo = new WhatDo(workWithFile, messageForClient, fileHandler, defaultAddress);
    }


    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        if (msg instanceof Command && !isChanelReg) {
            registrationProcess(ctx, msg);
        } else if (msg instanceof Command) {
            whatDo.whatDo((Command) msg, login);
        } else {
            LOGGER.log(Level.INFO, "Пришла неизвестная команда" + ctx);
        }

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        if (login != null) {
            LOGGER.log(Level.INFO, "Пользователь " + login + " отключился");
        } else {
            LOGGER.log(Level.INFO, "Неизвестный пользователь отключился не авторизовавшись");
        }
        ctx.close();
    }

    private void registrationProcess(ChannelHandlerContext ctx, Object msg) {
        while (!isChanelReg) {
            if (((Command) msg).getType().equals(TypeCommand.AUTH)) {
                LOGGER.log(Level.INFO, "Попытка авторизоваться" + ctx);
                String isLogin = baseAuthService.getUsernameByLoginAndPassword((Command) msg);
                if (isLogin == null) {
                    ctx.writeAndFlush(Command.authErrorCommand("ошибка авторизации, пароль или логин не верны"));
                    LOGGER.log(Level.INFO, "Ошибка авторизации" + ctx);
                    break;
                } else {
                    login = isLogin;
                    ctx.writeAndFlush(Command.authOkCommand(login));
                    whatDo.firstStep(login);
                    isChanelReg = true;
                    LOGGER.log(Level.INFO, "Удачная авторизация" + ctx);
                }
            } else {
                ctx.writeAndFlush(Command.message("server", "вы не зарегестрированы"));
                break;
            }
        }
    }
}

