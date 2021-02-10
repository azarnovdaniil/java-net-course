package ru.daniilazarnov;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import ru.daniilazarnov.database.DBService;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

public class ServerAuthHandler extends ChannelInboundHandlerAdapter {

    /**
     * Хэндлер для предварительной аутентификации
     */

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof AuthMessage) {
            DBService dbService = new DBService();
            AuthMessage am = (AuthMessage) msg;

            if (dbService.findUser(am.getLogin(), am.getPassword())) {
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

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }
}
