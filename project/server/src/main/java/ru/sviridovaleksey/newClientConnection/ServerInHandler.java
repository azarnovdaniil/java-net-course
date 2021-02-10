package ru.sviridovaleksey.newClientConnection;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import ru.sviridovaleksey.Command;
import ru.sviridovaleksey.TypeCommand;
import ru.sviridovaleksey.clientHandler.DataBaseUser;
import ru.sviridovaleksey.clientHandler.RegistrationProcess;
import ru.sviridovaleksey.commands.MessageCommandData;
import ru.sviridovaleksey.workwithfiles.WorkWithFile;

public class ServerInHandler extends ChannelInboundHandlerAdapter {

    private Boolean isChanelReg= false;
    private DataBaseUser dataBaseUser;
    private String login;
    private RegistrationProcess registrationProcess;
    private MessageForClient messageForClient;
    private WorkWithFile workWithFile;
    private WhatDo whatDo;



    public ServerInHandler(DataBaseUser dataBaseUser) {
        this.dataBaseUser = dataBaseUser;
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Подключился новый пользователь");
        ctx.write(Command.ping());
        this.messageForClient = new MessageForClient(ctx);
        this.workWithFile = new WorkWithFile(messageForClient);
        this.whatDo = new WhatDo(workWithFile);
        this.registrationProcess = new RegistrationProcess(dataBaseUser, workWithFile);
    }


    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        if (msg instanceof Command && !isChanelReg) {
           while (!isChanelReg) {

              if (((Command) msg).getType().equals(TypeCommand.AUTH)) {
                  System.out.println("Попытка авторизоваться" + ctx);
                  String isLogin = registrationProcess.isAuthOk((Command) msg);
                  if ( isLogin == null) {
                      ctx.write(Command.authErrorCommand("ошибка авторизации, пароль или логин не верны"));
                      System.out.println("Ошибка авторизации" + ctx);
                      break;
                  } else {
                      login = isLogin;
                      isChanelReg = true;
                      ctx.write(Command.authOkCommand(login));
                      System.out.println("Удачная авторизация" + ctx);

                  }

              } else  {
                  ctx.write(Command.message("server", "вы не зарегестрированы"));
                  break;
              }
           }
        }
        else if (msg instanceof Command && isChanelReg) {
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
