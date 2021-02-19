package ru.daniilazarnov.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.apache.log4j.Logger;
import ru.daniilazarnov.console_IO.OutputConsole;
import ru.daniilazarnov.enumeration.Command;
import ru.daniilazarnov.ReceivingFiles;
import ru.daniilazarnov.enumeration.State;
import ru.daniilazarnov.files_method.FileList;

/**
 * Класс содержит логику обработки принятых сообщений на стороне клиента
 */
public class ClientReadMessage extends ChannelInboundHandlerAdapter {
    private static final Logger LOG = Logger.getLogger(ClientReadMessage.class);
//    private State currentState = State.IDLE;

    public ClientReadMessage() {
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (ReceivingFiles.getCurrentState() == State.IDLE) {
            ByteBuf buf = ((ByteBuf) msg);
            byte readed = buf.readByte();
            Command command = Command.valueOf(readed);
            switch (command) {

                case DOWNLOAD:
                    ReceivingFiles.fileReceive(msg, "user1");
                    OutputConsole.printPrompt(); // вывод строки приглашения к вводу
                    break;
                case AUTH:
                        LOG.debug("Authorization: the response came from the server");
                    break;
                case LS:
                    System.out.println(FileList.getFilesListStringFromServer(buf));
                    OutputConsole.setConsoleBusy(false);
                    break;
                default:
                    System.err.println("(class ClientHandler) ERROR: Invalid first byte - " + readed);
            }
        }
        if (ReceivingFiles.getCurrentState() == State.FILE) {
            ReceivingFiles.fileReceive(msg, "user1");
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        LOG.error(cause);
    }
}
