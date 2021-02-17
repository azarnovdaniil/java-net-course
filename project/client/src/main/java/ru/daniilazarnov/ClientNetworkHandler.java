package ru.daniilazarnov;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.apache.log4j.Logger;
import ru.daniilazarnov.console_IO.OutputConsole;
import ru.daniilazarnov.files_method.FileList;

/**
 * Класс содержит логику обработки принятых сообщений на стороне клиента
 */
public class ClientNetworkHandler extends ChannelInboundHandlerAdapter {
    private static final Logger LOG = Logger.getLogger(ClientNetworkHandler.class);
    private static final String USER = "user1";
    private static boolean auth = false;
   private Auth authentication = new Auth();

    public static void setAuth(boolean auth) {
        ClientNetworkHandler.auth = auth;
    }

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
                     OutputConsole.printPrompt(); // вывод строки приглашения к вводу
                    break;
                case AUTH:
                    authentication.authentication(buf, ctx);
                    break;
                case LS:
                    System.out.println(FileList.getFilesListStringFromServer(buf));
                    OutputConsole.printPrompt();
                    break;

                default:
                    System.err.println("(class ClientHandler) ERROR: Invalid first byte - " + readed);
            }
        }
        if (ReceivingFiles.getCurrentState() == State.FILE) {
            ReceivingFiles.fileReceive(msg, USER);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        LOG.error(cause);
    }
}
