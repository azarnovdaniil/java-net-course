package ru.daniilazarnov.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.apache.log4j.Logger;
import ru.daniilazarnov.auth.AuthClient;
import ru.daniilazarnov.enumeration.Command;
import ru.daniilazarnov.ReceivingFiles;
import ru.daniilazarnov.enumeration.State;

/**
 * Класс содержит логику обработки принятых сообщений на стороне клиента
 */
public class ClientReadMessage extends ChannelInboundHandlerAdapter {
    private static final Logger LOG = Logger.getLogger(ClientReadMessage.class);
    private static boolean auth = false;
    private NetworkHandler nc = new NetworkHandler();

    public static void setAuth(boolean auth) {
        ClientReadMessage.auth = auth;
    }

    public static boolean isAuth() {
        return auth;
    }

    private State currentState = State.IDLE;

    public ClientReadMessage() {
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (ReceivingFiles.getCurrentState() == State.IDLE) {
            ByteBuf buf = ((ByteBuf) msg);
            byte readed = buf.readByte();
            Command command = Command.valueOf(readed);
            nc.networkHandler(ctx, msg, buf, readed, command);
        }
        if (ReceivingFiles.getCurrentState() == State.FILE) {
            ReceivingFiles.fileReceive(msg, AuthClient.getUserFolder());
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        LOG.error(cause);
    }
}
