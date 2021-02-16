package ru.daniilazarnov;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.apache.log4j.Logger;

/**
 * Класс содержит логику обработки принятых сообщений на стороне клиента
 */
public class ClientNetworkHandler extends ChannelInboundHandlerAdapter {
    private static final Logger LOG = Logger.getLogger(ClientNetworkHandler.class);
    private static final String USER = "user1";
    private static boolean auth = false;

    public static boolean isAuth() {
        return auth;
    }

    private State currentState = State.IDLE;

    public ClientNetworkHandler() {
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (ReceivingFiles.getCurrentState() == State.IDLE) {
            ByteBuf buf = ((ByteBuf) msg);
            byte readed = buf.readByte();
            Command command = Command.valueOf(readed);

            switch (command) {

                case DOWNLOAD:
                    ReceivingFiles.fileReceive(msg, USER);
                    InputConsole.printPrompt(); // вывод строки приглашения к вводу
                    break;
                case AUTH:
                    authentication(buf);
                    break;
                case LS:
                    getLSString(buf);
                    break;

                default:
                    System.err.println("(class ClientHandler) ERROR: Invalid first byte - " + readed);
            }
        }
        if (ReceivingFiles.getCurrentState() == State.FILE) {
            ReceivingFiles.fileReceive(msg, USER);
        }
    }

    private void getLSString(ByteBuf buf) {
        String catalogStrings = ReceivingAndSendingStrings.receiveAndEncodeString(buf);
        System.out.println(catalogStrings);
    }

    private void authentication(ByteBuf buf) {
        String right = ReceivingAndSendingStrings.receiveAndEncodeString(buf);
        if (right.equals("1")) {
            auth = true;
        }
        System.out.println(" [Доступ к удаленной базе " + (right.equals("1") ? "разрешен" : "отсутствует") + "]");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        LOG.error(cause);
    }
}
