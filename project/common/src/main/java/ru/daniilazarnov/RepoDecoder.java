package ru.daniilazarnov;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import java.nio.charset.StandardCharsets;
import java.util.function.BiConsumer;
import java.util.function.Consumer;


public class RepoDecoder extends ChannelInboundHandlerAdapter {

    private final boolean isServer;
    private final UserProfile profile;
    private final BiConsumer<ContextData, UserProfile> commandReader;
    private Consumer<UserProfile> closeConnection;

    RepoDecoder(boolean isServer, BiConsumer<ContextData, UserProfile> commandReader, Consumer<UserProfile> closeConnection) {
        this.isServer = isServer;
        this.profile = null;
        this.commandReader = commandReader;

    }

    RepoDecoder(boolean isServer, BiConsumer<ContextData, UserProfile> commandReader, Consumer<UserProfile> closeConnection, UserProfile profile) {
        this.isServer = isServer;
        this.profile = profile;
        this.commandReader = commandReader;
        this.closeConnection = closeConnection;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("message came");
        ByteBuf message = (ByteBuf) msg;
        ContextData messageContext = new ContextData();
        messageContext.setCommand(message.readInt());
        messageContext.setFilePath(decodeContext(message));
        messageContext.setLogin(decodeContext(message));
        messageContext.setPassword(decodeContext(message));
        byte[] bytes = new byte[message.readableBytes()];
        message.readBytes(bytes);
        message.release();
        messageContext.setContainer(bytes);

        if (isServer) {
            if (!isAuthorised()) {
                if (!(messageContext.getCommand() == CommandList.login.getNum()) &&
                        !(messageContext.getCommand() == CommandList.register.getNum())) {
                    this.profile.sendMessage("false%%%You are not authorised. Please login before you can use this service.");
                    return;
                }
            }
        }
        if (messageContext.getCommand() == CommandList.upload.getNum()) {
            FileContainer container = new FileContainer(bytes, messageContext.getFilePath());
            ctx.fireChannelRead(container);
        } else {
            commandReader.accept(messageContext, profile);
        }


    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Connection lost....");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        closeConnection.accept(this.profile);
    }

    public String decodeContext(ByteBuf source) {
        int length = source.readInt();
        byte[] context = new byte[length];
        for (int i = 0; i < length; i++) {
            context[i] = source.readByte();
        }
        return new String(context, StandardCharsets.UTF_8);
    }

    private boolean isAuthorised() {
        return (!this.profile.getLogin().equals("empty"));
    }

}
