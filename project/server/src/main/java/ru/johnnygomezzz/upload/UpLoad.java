package ru.johnnygomezzz.upload;


import ru.johnnygomezzz.FileMessage;
import ru.johnnygomezzz.FileRequest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class UpLoad {
    private static final String PATH_STORAGE = ("project/server/storage/");

    public static void upLoad(io.netty.channel.ChannelHandlerContext ctx, Object msg) throws IOException {
        FileRequest fr = (FileRequest) msg;
        if (Files.exists(Paths.get(PATH_STORAGE + fr.getFilename()))) {
            FileMessage fm = new FileMessage(Paths.get(PATH_STORAGE + fr.getFilename()));
            ctx.writeAndFlush(fm);
            System.out.println("Файл " + fm.getFileName() + " успешно отправлен.");
        }
    }
}
