package ru.sviridovaleksey.newClientConnection;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import ru.sviridovaleksey.Command;
import ru.sviridovaleksey.TypeCommand;
import ru.sviridovaleksey.clientHandler.BaseAuthService;
import ru.sviridovaleksey.workwithfiles.WorkWithFile;

public class ServerInHandler extends ChannelInboundHandlerAdapter {

    private Boolean isChanelReg= false;
    private String login;
    private WhatDo whatDo;
    private final BaseAuthService baseAuthService = new BaseAuthService();



    @Override
    public void channelRegistered(ChannelHandlerContext ctx) {
        System.out.println("Подключился новый пользователь");
        ctx.write(Command.ping());
        MessageForClient messageForClient = new MessageForClient(ctx);
        WorkWithFile workWithFile = new WorkWithFile(messageForClient);
        this.whatDo = new WhatDo(workWithFile, messageForClient);
    }


    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        if (msg instanceof Command && !isChanelReg) {
           while (!isChanelReg) {

              if (((Command) msg).getType().equals(TypeCommand.AUTH)) {
                  System.out.println("Попытка авторизоваться" + ctx);
                  String isLogin = baseAuthService.getUsernameByLoginAndPassword((Command) msg);
                  if ( isLogin == null) {
                      ctx.write(Command.authErrorCommand("ошибка авторизации, пароль или логин не верны"));
                      System.out.println("Ошибка авторизации" + ctx);
                      break;
                  } else {
                      login = isLogin;
                      ctx.write(Command.authOkCommand(login));
                      whatDo.firstStep(login);
                      isChanelReg = true;
                      System.out.println("Удачная авторизация" + ctx);

                  }

              } else  {
                  ctx.write(Command.message("server", "вы не зарегестрированы"));
                  break;
              }
           }
        }
        else if (msg instanceof Command) {
            whatDo.whatDo((Command) msg, login);
        }

        else {
            System.out.println("Неизвестная команда");
        }

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
