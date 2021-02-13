package ru.daniilazarnov;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.apache.log4j.Logger;
import ru.daniilazarnov.database.DBService;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ServerAuthHandler extends ChannelInboundHandlerAdapter {
    private static final Logger LOGGER = Logger.getLogger(ServerAuthHandler.class.getName());

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof DBMessage) {
            DBService dbService = new DBService();
            DBMessage dbm = (DBMessage) msg;
            String dbCmd = dbm.getCmd();

            switch (dbCmd) {
                case "auth":
                    LOGGER.info("Authentication request from client");
                    if (dbService.findUser(dbm.getLogin(), dbm.getPassword())) {
                        DBMessage authOK = new DBMessage(dbm.getLogin());
                        Path newServerDir = Paths.get("./project/server_vault/" + dbm.getLogin());
                        if (!newServerDir.toFile().exists()) {
                            Files.createDirectories(newServerDir);
                        }
                        ctx.writeAndFlush(authOK);
                        ctx.pipeline().remove(this);

                    }
                    break;
                case "reg":
                    LOGGER.info("Registration request from new client");
                    dbService.addUser(dbm.getLogin(), dbm.getPassword());
                    break;

            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        LOGGER.error("SWW at auth handler", cause);
        super.exceptionCaught(ctx, cause);
    }
}
