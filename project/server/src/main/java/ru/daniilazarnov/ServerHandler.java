package ru.daniilazarnov;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.nio.file.Path;

/**
 * Класс содержит обработку принятых сообщений на стороне сервера
 */
public class ServerHandler extends ChannelInboundHandlerAdapter {
    private static final Logger log = Logger.getLogger(ServerHandler.class);
    private final String user = "server";
    private State currentState = State.IDLE;
    public static final String HOME_FOLDER_PATH = "project/server/cloud_storage/user1";


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
                    LSHandle(ctx, buf);
                    break;

                default:
                    invalidControlByte(buf, "(class ServerHandler) ERROR: Invalid first byte - ", readed);

            }
        }

        if (ReceivingFiles.getCurrentState() == State.FILE) {
            uploadFileToServer(msg);
        }

    }




//    }

    private void LSHandle(ChannelHandlerContext ctx, ByteBuf buf) throws IOException {
        String folderName = ReceivingAndSendingStrings.receiveAndEncodeString(buf);

        String fileList = UtilMethod.getFolderContents(folderName, "server");

        ReceivingAndSendingStrings
                .sendString(("\n" + fileList), ctx.channel(), (byte) 4,
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
//                ctx.fireChannelRead(buf);
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
        return; // TODO: 09.02.2021 удалить
    }

    /**
     * Загружает файл на сервер
     *
     */
    private void uploadFileToServer(Object msg) throws IOException {
        ReceivingFiles.fileReceive(msg, user);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        System.err.println(cause.getMessage());
        log.error(cause);
    }
}
