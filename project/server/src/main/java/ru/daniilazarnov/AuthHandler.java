package ru.daniilazarnov;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

//import com.mysql.jdbc.Driver
//import helpers.DataBaseHelper;
import java.util.Set;
import java.util.logging.Logger;

public class AuthHandler extends ChannelInboundHandlerAdapter {

    private Logger logger = Logger.getLogger(this.getClass().getName());
    //private final Set<CredentialsEntry> authorizedClients = DataBaseHelper.getUsers();
    private final Set<CredentialsEntry> authorizedClients = Set.of(
            new CredentialsEntry("l1", "p1", "nickname1"),
            new CredentialsEntry("l2", "p2", "nickname2"),
            new CredentialsEntry("l3", "p3", "nickname3"));

    private boolean authOk = false;


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (authOk) {
            ctx.fireChannelRead((String) msg);
            return;
        }
        String msgs = (String) msg;
        if (msgs.split("\\s")[0].equals("/auth")) {
            String username = (msgs.split(" "))[1];
            if (authorizedClients.contains(username)) {
                authOk = true;
                ctx.pipeline().addLast(new ServerHandler(username));
            }
        }
    }

    public String findNicknameByLoginAndPassword(String login, String password) {
        for (CredentialsEntry entry : authorizedClients) {
            if (entry.getLogin().equals(login) && entry.getPassword().equals(password)) {
                return entry.getNickname();
            }
        }
        return null;
    }
}
