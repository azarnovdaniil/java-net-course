package ru.daniilazarnov;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.apache.log4j.Logger;
import ru.daniilazarnov.enumeration.Command;
import ru.daniilazarnov.enumeration.State;
import ru.daniilazarnov.util.UtilMethod;

import java.io.IOException;


/**
 * Класс содержит обработку принятых сообщений на стороне сервера
 */
public class ServerHandler extends ChannelInboundHandlerAdapter {
    private static final Logger LOG = Logger.getLogger(ServerHandler.class);
    private final FilesMethod filesMethod = new FilesMethod();
    private final AuthServer authServer = new AuthServer();

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        System.out.println("Клиент подключился");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        System.err.println("Клиент отключился");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = ((ByteBuf) msg);
        if (ReceivingFiles.getCurrentState() == State.IDLE) {
            byte readed = buf.readByte();
            Command command = Command.valueOf(readed);
            switch (command) {
                case DOWNLOAD:
                    filesMethod.uploadFileToServer(buf); //2
                    break;
                case UPLOAD:
                    filesMethod.downloadFileFromServer(ctx, buf);  //1
                    break;
                case LS:
                    fileListSend(ctx, buf);
                    break;
                case AUTH:
                    LOG.info("Request authorization");
                    authServer.getAuth(ctx, buf);
                    break;
                default:
                    System.err.println(invalidControlByte(buf, readed));
                    throw new IllegalStateException("Unexpected value: " + readed);
            }
        }

        if (ReceivingFiles.getCurrentState() == State.FILE) {
            filesMethod.uploadFileToServer(msg);
        }
    }

    private void fileListSend(ChannelHandlerContext ctx, ByteBuf buf) throws IOException {
        String folderName = ReceivingAndSendingStrings.receiveAndEncodeString(buf);
        String fileList = UtilMethod.getFolderContents(folderName, "server");
        ReceivingAndSendingStrings
                .sendString((fileList), ctx.channel(), Command.LS.getCommandByte(),
                        UtilMethod.getChannelFutureListener("Список файлов успешно передан"));
    }

    /**
     * Будет содержать действия по умолчанию, если переданная команда окажется неизвестной
     */
    private String invalidControlByte(ByteBuf buf, byte readed) {
        buf.resetReaderIndex();
        return "(class ServerHandler) ERROR: Invalid first byte - " + readed;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        System.err.println(cause.getMessage());
        LOG.error(cause);
    }
}
