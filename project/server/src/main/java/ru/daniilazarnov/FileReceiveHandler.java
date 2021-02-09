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
public class FileReceiveHandler extends ChannelInboundHandlerAdapter {
    private static final Logger log = Logger.getLogger(FileReceiveHandler.class);
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

        if (currentState == State.IDLE) {
            byte readed = buf.readByte();

            switch (readed) {
                case (byte) 25:
                    uploadFileToServer(buf);
                    break;

                case (byte) 1:
                    downloadFileFromServer(ctx, buf);
                    break;

                default:
                    invalidControlByte(buf, "(class FileReceiveHandler) ERROR: Invalid first byte - ", readed);

            }
        }
    }

    /**
     * Будет содержать действия по умолчанию, если переданная команда окажется неизвестной
     *
     * @param buf ;
     * @param s ;
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
     * @param buf   ;
     * @throws IOException ;
     */
    private void downloadFileFromServer(ChannelHandlerContext ctx, ByteBuf buf) throws IOException {
        String fileName = UtilMethod.receiveAndEncodeString(buf);
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
     * @param buf буфер
     */
    private void uploadFileToServer(ByteBuf buf) throws IOException {
//        invalidControlByte(buf, "STATE: Start file receiving");
        ReceivingFiles.fileReceive(buf, user);
        buf.clear();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        System.err.println(cause.getMessage());
        cause.printStackTrace();
        log.error(cause);
//        ctx.close();
    }
}
