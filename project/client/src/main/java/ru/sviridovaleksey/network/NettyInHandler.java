package ru.sviridovaleksey.network;

import io.netty.channel.*;
import ru.sviridovaleksey.Command;
import ru.sviridovaleksey.TypeCommand;
import ru.sviridovaleksey.commands.AuthOkCommandData;
import ru.sviridovaleksey.interactionwithuser.HelloMessage;
import ru.sviridovaleksey.interactionwithuser.Interaction;

import java.util.Scanner;


public class NettyInHandler extends ChannelInboundHandlerAdapter {

    private Interaction interaction;
    private WhatDoClient whatDoClient;
    private HelloMessage helloMessage = new HelloMessage();
    private Scanner scanner = new Scanner(System.in);
    private Boolean autoK = false;
    private String userName;

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        interaction = new Interaction(ctx.channel());
        whatDoClient = new WhatDoClient();

    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        if (msg instanceof Command) {

            whatDoClient.whatDoClient((Command) msg);

            if (((Command) msg).getType().equals(TypeCommand.AUTH_OK)) {
                autoK =true;
                AuthOkCommandData data = (AuthOkCommandData) ((Command) msg).getData();
                this.userName = data.getUsername();
                Thread thread = new Thread(() -> {
                    interaction.startInteraction(helloMessage, userName);
                });
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
