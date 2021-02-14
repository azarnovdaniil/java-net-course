package ru.daniilazarnov;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.apache.log4j.Logger;
import ru.daniilazarnov.sql_client.SQLClient;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Класс содержит обработку принятых сообщений на стороне сервера
 */
public class ServerHandler extends ChannelInboundHandlerAdapter {
    private static final Logger LOG = Logger.getLogger(ServerHandler.class);
    private final String server = "server";
    private State currentState = State.IDLE;
    public static final String USER = "user1";
    public static final String HOME_FOLDER_PATH = Path.of("project", "server", "cloud_storage", USER)
            .toString() + File.separator;
    private static final int FOUR = 4;

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
                    uploadFileToServer(buf); //2
                    break;
                case UPLOAD:
                    downloadFileFromServer(ctx, buf);  //1
                    break;
                case LS:
                    lsHandle(ctx, buf);
                    break;
                case AUTH:
                    LOG.info("Запрос авторизации");
                    getAuth(buf);


                    break;
                default:
                    invalidControlByte(buf, "(class ServerHandler) ERROR: Invalid first byte - ", readed);
            }
        }

        if (ReceivingFiles.getCurrentState() == State.FILE) {
            uploadFileToServer(msg);
        }
    }

    private void getAuth(ByteBuf buf) {
        String rawAuthData = ReceivingAndSendingStrings.receiveAndEncodeString(buf);
        List<String> authData = Arrays.stream(rawAuthData.split("%-%")).collect(Collectors.toList());
        System.out.println(SQLClient.getAuth(authData.get(0), authData.get(1)));
        System.out.println(authData);
    }

    private void lsHandle(ChannelHandlerContext ctx, ByteBuf buf) throws IOException {
        String folderName = ReceivingAndSendingStrings.receiveAndEncodeString(buf);
        String fileList = UtilMethod.getFolderContents(folderName, "server");
        ReceivingAndSendingStrings
                .sendString(("\n" + fileList), ctx.channel(), (byte) FOUR,
                        UtilMethod.getChannelFutureListener("Список файлов успешно передан"));
        System.out.println(folderName);
    }

    /**
     * Будет содержать действия по умолчанию, если переданная команда окажется неизвестной
     *
     * @param buf    ;
     * @param s      ;
     * @param readed ;
     */
    private void invalidControlByte(ByteBuf buf, String s, byte readed) {
        System.out.println(s + readed);
        buf.resetReaderIndex();
        throw new IllegalStateException("Unexpected value: " + readed);
    }

    /**
     * Скачивает файл с сервера и передает в сторону клиента
     *
     * @param ctx ;
     * @param buf ;
     * @throws IOException ;
     */
    private void downloadFileFromServer(ChannelHandlerContext ctx, ByteBuf buf) throws IOException {
        String fileName = ReceivingAndSendingStrings.receiveAndEncodeString(buf);
        System.out.println("fileName ".toUpperCase() + fileName);

        System.out.println("STATE: Start file download");
        FileSender.sendFile(Path.of(HOME_FOLDER_PATH, fileName),
                ctx.channel(),
                UtilMethod.getChannelFutureListener("Файл успешно передан"));
        buf.clear();
    }

    /**
     * Загружает файл на сервер
     */
    private void uploadFileToServer(Object msg) throws IOException {
        ReceivingFiles.fileReceive(msg, server);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        System.err.println(cause.getMessage());
        LOG.error(cause);
    }
}
