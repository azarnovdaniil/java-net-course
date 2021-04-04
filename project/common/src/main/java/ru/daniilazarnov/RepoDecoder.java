package ru.daniilazarnov;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.charset.StandardCharsets;
import java.util.function.BiConsumer;
import java.util.function.Consumer;


public class RepoDecoder <T extends PathHolder> extends ChannelInboundHandlerAdapter {

    private final T profile;
    private final BiConsumer<ContextData, T> commandReader;
    private final Consumer<T> closeConnection;
    private static final Logger LOGGER = LogManager.getLogger(RepoDecoder.class);

    RepoDecoder(BiConsumer<ContextData, T> commandReader, Consumer<T> closeConnection, T profile) {
        this.profile = profile;
        this.commandReader = commandReader;
        this.closeConnection = closeConnection;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        LOGGER.info("Byte package arrived.");
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


        if (!isAuthorised()) {
            if (!(messageContext.getCommand() == CommandList.login.getNum()) &&
                    !(messageContext.getCommand() == CommandList.register.getNum())) {
                LOGGER.info("User not authorised, package refused");
                this.profile.sendMessage("false%%%You are not authorised. Please login before you can use this service.");
                return;
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
    public void channelInactive(ChannelHandlerContext ctx) {
        closeConnection.accept(profile);
        LOGGER.info("Connection lost...");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
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
