package ru.daniilazarnov;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import static ru.daniilazarnov.constants.Constants.DEFAULT_PATH_SERVER;

public class FilesMethod {
    private final String server = "server";
    private final String user = "user1";

    /**
     * Скачивает файл с сервера и передает в сторону клиента
     *
     * @param ctx ;
     * @param buf ;
     * @throws IOException ;
     */
     void downloadFileFromServer(ChannelHandlerContext ctx, ByteBuf buf) throws IOException {
        String fileName = ReceivingAndSendingStrings.receiveAndEncodeString(buf);
        System.out.println("fileName ".toUpperCase() + fileName);

        System.out.println("STATE: Start file download");
        FileSender.sendFile(Path.of(DEFAULT_PATH_SERVER + File.separator + user + File.separator, fileName),
                ctx.channel(),
                UtilMethod.getChannelFutureListener("Файл успешно передан"));
        buf.clear();
    }

    /**
     * Загружает файл на сервер
     */
    void uploadFileToServer(Object msg) throws IOException {
        ReceivingFiles.fileReceive(msg, server);
    }
}
