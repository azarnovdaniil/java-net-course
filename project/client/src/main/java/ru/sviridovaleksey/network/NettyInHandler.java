package ru.sviridovaleksey.network;

import io.netty.channel.*;
import ru.sviridovaleksey.Client;
import ru.sviridovaleksey.Command;
import ru.sviridovaleksey.TypeCommand;
import ru.sviridovaleksey.commands.AuthOkCommandData;
import ru.sviridovaleksey.interactionwithuser.HelloMessage;
import ru.sviridovaleksey.interactionwithuser.Interaction;
import ru.sviridovaleksey.workwithfile.WorkWithFileClient;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Scanner;



public class NettyInHandler extends ChannelInboundHandlerAdapter {

    private Interaction interaction;
    private WhatDoClient whatDoClient;
    private final HelloMessage helloMessage = new HelloMessage();
    private final Scanner scanner = new Scanner(System.in);
    private Boolean autoK = false;
    private String userName;
    private final WorkWithFileClient workWithFileClient = new WorkWithFileClient();
    private String defaultAddress;

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) {
        try {
            String path = Client.class.getProtectionDomain().getCodeSource().getLocation().getPath();
            defaultAddress = URLDecoder.decode(path, "UTF-8");
            defaultAddress = new File(new File(defaultAddress).getParent()).getParent();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        interaction = new Interaction(ctx.channel());
        whatDoClient = new WhatDoClient(defaultAddress);

    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {

    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        final String whiteSpace = "/";

        if (msg instanceof Command) {

            whatDoClient.whatDoClient((Command) msg);
            if (((Command) msg).getType().equals(TypeCommand.AUTH_OK)) {
                autoK = true;
                AuthOkCommandData data = (AuthOkCommandData) ((Command) msg).getData();
                this.userName = data.getUsername();
                ctx.writeAndFlush(Command.getShowDir(userName, whiteSpace));
                Thread thread = new Thread(() -> interaction.startInteraction(helloMessage,
                        userName, workWithFileClient, defaultAddress));
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
