package ru.daniilazarnov;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

public class ServerAuthHandler extends ChannelInboundHandlerAdapter {

    /**
     * Хэндлер для предварительной аутентификации
     */

    private static final Map<String, String> USERS = Map.of(
            "user1" , "1",
            "user2" , "2",
            "user3" , "3"
    );

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof AuthMessage) {
            AuthMessage am = (AuthMessage) msg;

            for (Map.Entry entry : USERS.entrySet()) {
                if ((am.getLogin().equals(entry.getKey())) && (am.getPassword().equals(entry.getValue()))) {
                    AuthMessage authOK = new AuthMessage(am.getLogin(),"");
                    Path newServerDir = Paths.get("./project/server/src/main/java/ru/daniilazarnov/server_vault/" + am.getLogin());
                    if (!newServerDir.toFile().exists()) {
                        Files.createDirectories(newServerDir);
                    }
                    ctx.writeAndFlush(authOK);
                    ctx.pipeline().remove(this);
                }
            }
        }
    }

}
