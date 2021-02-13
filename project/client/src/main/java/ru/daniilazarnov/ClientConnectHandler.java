package ru.daniilazarnov;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.apache.log4j.Logger;

/**
 * Класс содержит логику обработки принятых сообщений на стороне клиента
 */
public class ClientConnectHandler extends ChannelInboundHandlerAdapter {
    private static final Logger log = Logger.getLogger(ClientConnectHandler.class);
    private static final String user ="user1";
    private State currentState = State.IDLE;

    public ClientConnectHandler() {
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (ReceivingFiles.getCurrentState() == State.IDLE) {
        ByteBuf buf = ((ByteBuf) msg);
            byte readed = buf.readByte();
        Command command = Command.valueOf(readed);

            switch (command) {
                case DOWNLOAD:
                    ReceivingFiles.fileReceive(msg, user);
                    Client.printPrompt(); // вывод строки приглашения к вводу
                    break;

                case LS:
                    String catalogStrings = ReceivingAndSendingStrings.receiveAndEncodeString(buf);
                    System.out.println(catalogStrings);
                    break;

                default:
                    System.err.println("(class ClientHandler) ERROR: Invalid first byte - " + readed);
            }
        }
        if (ReceivingFiles.getCurrentState() == State.FILE) {
            ReceivingFiles.fileReceive(msg, user);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause){
        log.error(cause);
    }
}