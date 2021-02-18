package ru.daniilazarnov;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import org.apache.log4j.Logger;
import ru.daniilazarnov.enumeration.Command;
import ru.daniilazarnov.sql_client.SQLClient;
import ru.daniilazarnov.util.UtilMethod;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class AuthServer {
    private static final Logger LOG = Logger.getLogger(AuthServer.class);


    void getAuth(ChannelHandlerContext ctx, ByteBuf buf) {
        String rawAuthData = getString(buf);
        List<String> authData = getAuthData(rawAuthData);
        int accessRights = SQLClient.getAuth(authData.get(0), authData.get(1));
        ReceivingAndSendingStrings
                .sendString(accessRights + "",
                        ctx.channel(),
                        Command.AUTH.getCommandByte(),
                        UtilMethod
                                .getChannelFutureListener(String
                                        .format("Авторизация %s подтверждена", accessRights == 1 ? "" : "не")));
        if (accessRights == 1) {
            LOG.info(String.format("Authorization confirmed, user: [%s]", authData.get(0)));
        } else {
            LOG.info("Authorization not verified, connection dropped");
            ctx.close();

        }
    }

    private List<String> getAuthData(String rawAuthData) {
        return Arrays.stream(rawAuthData.split("%-%")).collect(Collectors.toList());
    }

    private String getString(ByteBuf buf) {
        return ReceivingAndSendingStrings.receiveAndEncodeString(buf);
    }
}
