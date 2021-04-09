package ru.daniilazarnov;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import messages.AuthMessage;
import messages.Message;
import messages.MessageType;

import java.util.Set;

public class AuthHandler extends ChannelInboundHandlerAdapter {

    //private final Set<AuthMessage> authorizedClients = DataBaseHelper.getUsers();
    private final Set<AuthMessage> authorizedClients = Set.of(
            new AuthMessage("l1", "p1", "nickname1"),
            new AuthMessage("l2", "p2", "nickname2"),
            new AuthMessage("l3", "p3", "nickname3"));

    private boolean authOk = false;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        if (authOk) {
            ctx.fireChannelRead(msg);
            return;
        }

        Message msgs = (Message) msg;
        if (msgs.getType().equals(MessageType.AUTHORIZATION)) {

            AuthMessage cr = (AuthMessage) msgs.getMessage();

            if (authorizedClients.stream().anyMatch(x -> x.getLogin().equals(cr.getLogin()))) {
                authOk = true;
                ctx.channel().writeAndFlush("ok");
                ctx.pipeline().addLast(new ServerHandler(cr.getLogin()));
            }
        }
    }

    public String findNicknameByLoginAndPassword(String login, String password) {
        for (AuthMessage entry : authorizedClients) {
            if (entry.getLogin().equals(login) && entry.getPassword().equals(password)) {
                return entry.getNickname();
            }
        }
        return null;
    }
}
