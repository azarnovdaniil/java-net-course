package ru.daniilazarnov.common.commands.login;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import ru.daniilazarnov.common.CommonPackageConstants;
import ru.daniilazarnov.common.commands.Command;
import ru.daniilazarnov.common.commands.Commands;

import java.nio.charset.StandardCharsets;

public class LoginCommand implements Command {

    private Channel channel;
    private String login;
    private String password;

    public LoginCommand(Channel channel, String login, String password) {
        this.channel = channel;
        this.login = login;
        this.password = password;
    }

    @Override
    public void execute() {
        sendLoginCommand(
                future -> {
                    if (!future.isSuccess()) {
                        future.cause().printStackTrace();
                    }
                    if (future.isSuccess()) {
                        System.out.println("Login command has been sent");
                    }
                });

    }

    private void sendLoginCommand(ChannelFutureListener finishListener) {
        ByteBuf buf = ByteBufAllocator.DEFAULT.directBuffer(1);
        buf.writeByte(Commands.LOGIN.getCode());
        channel.writeAndFlush(buf);

        byte[] loginBytes = login.getBytes(StandardCharsets.UTF_8);
        buf = ByteBufAllocator.DEFAULT.directBuffer(CommonPackageConstants.CONTENT_LENGTH_BYTES.getCode());
        buf.writeInt(loginBytes.length);
        channel.writeAndFlush(buf);

        buf = ByteBufAllocator.DEFAULT.directBuffer(loginBytes.length);
        buf.writeBytes(loginBytes);
        channel.writeAndFlush(buf);

        byte[] passwordBytes = password.getBytes(StandardCharsets.UTF_8);
        buf = ByteBufAllocator.DEFAULT.directBuffer(CommonPackageConstants.CONTENT_LENGTH_BYTES.getCode());
        buf.writeInt(passwordBytes.length);
        channel.writeAndFlush(buf);

        buf = ByteBufAllocator.DEFAULT.directBuffer(passwordBytes.length);
        buf.writeBytes(passwordBytes);
        ChannelFuture transferOperationFuture = channel.writeAndFlush(buf);

        if (finishListener != null) {
            transferOperationFuture.addListener(finishListener);
        }
    }

}
