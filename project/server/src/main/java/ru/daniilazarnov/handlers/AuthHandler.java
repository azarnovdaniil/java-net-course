package ru.daniilazarnov.handlers;


import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.apache.log4j.Logger;
import ru.daniilazarnov.DB.DBService;
import ru.daniilazarnov.DBMsg;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class AuthHandler extends ChannelInboundHandlerAdapter {
    private static final Logger LOGGER = Logger.getLogger(AuthHandler.class.getName());

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof DBMsg) {
            DBService dbService = new DBService();
            DBMsg dbm = (DBMsg) msg;
            String dbCmd = dbm.getCmd();

            switch (dbCmd) {
                case "/auth":
                    LOGGER.info("Запрос на авторизацию от клиента");
                    if (dbService.findUser(dbm.getLogin(), dbm.getPassword())) {
                        DBMsg authOK = new DBMsg(dbm.getLogin());
                        Path newServerDir = Paths.get("./project/server_dir/" + dbm.getLogin());
                        if (!newServerDir.toFile().exists()) {
                            Files.createDirectories(newServerDir);
                        }
                        ctx.writeAndFlush(authOK);
                        ctx.pipeline().remove(this);

                    }
                    break;
                case "/reg":
                    LOGGER.info("Запрос на регистрацию новго клиента");
                    dbService.addUser(dbm.getLogin(), dbm.getPassword());
                    break;
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        LOGGER.error("Сбой в работе обработчика авторизации", cause);
        super.exceptionCaught(ctx, cause);
    }
}