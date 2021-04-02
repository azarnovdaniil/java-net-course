package ru.daniilazarnov;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.charset.StandardCharsets;
import java.util.function.Consumer;


public class RepoDecoderClient extends ChannelInboundHandlerAdapter {

    private final Consumer<ContextData> commandReader;
    private final Consumer<Boolean> closeConnection;

    RepoDecoderClient(Consumer<ContextData> commandReader, Consumer<Boolean> closeConnection) {
        this.commandReader = commandReader;
        this.closeConnection = closeConnection;
    }


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
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

        if (messageContext.getCommand() == CommandList.upload.getNum()) {
            FileContainer container = new FileContainer(bytes, messageContext.getFilePath());
            ctx.fireChannelRead(container);
        } else {
            commandReader.accept(messageContext);
        }


    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        System.out.println("Connection lost....");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        closeConnection.accept(true);
    }

    public String decodeContext(ByteBuf source) {
        int length = source.readInt();
        byte[] context = new byte[length];
        for (int i = 0; i < length; i++) {
            context[i] = source.readByte();
        }
        return new String(context, StandardCharsets.UTF_8);
    }


}
