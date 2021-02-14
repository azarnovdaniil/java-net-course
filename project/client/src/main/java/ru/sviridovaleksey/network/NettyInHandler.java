package ru.sviridovaleksey.network;

import io.netty.channel.*;
import ru.sviridovaleksey.Command;
import ru.sviridovaleksey.TypeCommand;
import ru.sviridovaleksey.commands.AuthOkCommandData;
import ru.sviridovaleksey.interactionwithuser.HelloMessage;
import ru.sviridovaleksey.interactionwithuser.Interaction;
import ru.sviridovaleksey.workwithfile.WorkWithFileClient;

import java.util.Scanner;


public class NettyInHandler extends ChannelInboundHandlerAdapter {

    private Interaction interaction;
    private WhatDoClient whatDoClient;
    private final HelloMessage helloMessage = new HelloMessage();
    private final Scanner scanner = new Scanner(System.in);
    private Boolean autoK = false;
    private String userName;
    private final WorkWithFileClient workWithFileClient = new WorkWithFileClient();

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) {
        interaction = new Interaction(ctx.channel());
        whatDoClient = new WhatDoClient();

    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {

    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        if (msg instanceof Command) {

            whatDoClient.whatDoClient((Command) msg);

            if (((Command) msg).getType().equals(TypeCommand.AUTH_OK)) {
                autoK =true;
                AuthOkCommandData data = (AuthOkCommandData) ((Command) msg).getData();
                this.userName = data.getUsername();
                ctx.write(Command.getShowDir(userName,""));
                Thread thread = new Thread(() -> interaction.startInteraction(helloMessage, userName, workWithFileClient));
                thread.start();
            }

            if (!autoK) {
                helloMessage.needRegMessage();
                String login = scanner.next();
                String password = scanner.next();
                ctx.writeAndFlush(Command.authCommand(login, password));
            }


        }
    }

}
