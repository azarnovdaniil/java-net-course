package ru.daniilazarnov.handlers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.log4j.Logger;
import ru.daniilazarnov.DirectoryListInfo;
import ru.daniilazarnov.FileMsg;
import ru.daniilazarnov.RequestMsg;

public class RequestHandler extends ChannelInboundHandlerAdapter {

    private static final Logger LOGGER = Logger.getLogger(RequestHandler.class.getName());

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof RequestMsg) {
            RequestMsg request = (RequestMsg) msg;
            String clientCmd = request.getCmd();

            switch (clientCmd) {
                case "/download":
                    try {
                        if (Files.exists(Paths.get("./project/server_dir/" + request.getLogin() + "/" + request.getFilename()))) {
                            File file = new File("./project/server_dir/" + request.getLogin() + "/" + request.getFilename());
                            int bufSize = 1024 * 1024 * 10;
                            int partsCount = (int) (file.length() / bufSize);
                            if (file.length() % bufSize != 0) {
                                partsCount++;
                            }

                            FileMsg fm = new FileMsg(file.getName(), -1, partsCount, new byte[bufSize], request.getLogin());
                            FileInputStream in = new FileInputStream(file);

                            LOGGER.debug("Загрузка началась");
                            for (int i = 0; i < partsCount; i++) {
                                int readedBytes = in.read(fm.getData());
                                fm.setPartNumber(i + 1);
                                if (readedBytes < bufSize) {
                                    fm.setData(Arrays.copyOfRange(fm.getData(), 0, readedBytes));
                                }
                                ctx.writeAndFlush(fm);
                                LOGGER.debug("Part: " + (i + 1) + " отправлена");
                                System.out.println(fm.getPartNumber());
                            }
                            in.close();
                            LOGGER.debug("Файл удачно загружен клиентом");
                        }
                    } catch (IOException e) {
                        LOGGER.error("Сбой при отправке файла клиентом", e);
                        throw new RuntimeException("SWW", e);
                    }
                    break;
                case "/ls":
                    LOGGER.debug("Получена комманда /la (список файлов) от клиента");
                    List<String> files = new ArrayList<>();
                    Files.walkFileTree(Paths.get("./project/server_dir/" + request.getLogin()),
                            new SimpleFileVisitor<Path>() {
                                @Override
                                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                                    files.add(file.getFileName().toString());
                                    return FileVisitResult.CONTINUE;
                                }
                            });
                    DirectoryListInfo dim = new DirectoryListInfo(files);
                    ctx.writeAndFlush(dim);
                    break;
                case "/rm":
                    LOGGER.debug("Получена комманда /rm (удаление) от клиента");
                    Path removeDir = Paths.get("./project/server_dir/" + request.getLogin() + "/" + request.getFilename());
                    if (Files.exists(removeDir)) {
                        removeDir.toFile().delete();
                    }
                    break;
                case "/mv":
                    LOGGER.debug("Получена комманда /mw (переименование) от клиента");
                    Path renameDir = Paths.get("./project/server_dir/" + request.getLogin() + "/" + request.getFilename());
                    Path newDir = Paths.get("./project/server_dir/" + request.getLogin() + "/" + request.getNewFileName());
                    if (Files.exists(renameDir)) {
                        renameDir.toFile().renameTo(newDir.toFile());
                    }
                    break;
            }
        } else ctx.fireChannelRead(msg);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        LOGGER.info("Клиент подключился");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        LOGGER.info("Клиент отключился");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        LOGGER.error("Сбой при работе обработчика авторизации", cause);
        super.exceptionCaught(ctx, cause);
    }
}

