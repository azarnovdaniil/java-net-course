package ru.daniilazarnov;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ServerRequestHandler extends ChannelInboundHandlerAdapter {

    /**
     * Хэндлер для обработки запросов пришедших от клиента
     */

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof RequestMessage) {
            RequestMessage request = (RequestMessage) msg;
            String clientCmd = request.getCmd();

            switch (clientCmd) {
                case "download":
                    try {
                        if (Files.exists(Paths.get("./project/server_vault/" + request.getLogin() + "/" + request.getFilename()))) {
                            File file = new File("./project/server_vault/" + request.getLogin() + "/" + request.getFilename());
                            int bufSize = 1024 * 1024 * 10;
                            int partsCount = (int) (file.length() / bufSize);
                            if (file.length() % bufSize != 0) {
                                partsCount++;
                            }

                            FileMessage fm = new FileMessage(file.getName(), -1, partsCount, new byte[bufSize], request.getLogin());
                            FileInputStream in = new FileInputStream(file);

                            for (int i = 0; i < partsCount; i++) {
                                int readedBytes = in.read(fm.getData());
                                fm.setPartNumber(i + 1);
                                if (readedBytes < bufSize) {
                                    fm.setData(Arrays.copyOfRange(fm.getData(), 0, readedBytes));
                                }
                                ctx.writeAndFlush(fm);
                                System.out.println("Отправлена часть: " + (i + 1));
                                System.out.println(fm.getPartNumber());
                            }
                            in.close();
                        }
                    } catch (IOException e) {
                        throw new RuntimeException("SWW", e);
                    }
                    break;
                case "list":
                    List<String> files = new ArrayList<>();
                    Files.walkFileTree(Paths.get("./project/server_vault/" + request.getLogin()),
                            new SimpleFileVisitor<Path>() {
                                @Override
                                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                                    files.add(file.getFileName().toString());
                                    return FileVisitResult.CONTINUE;
                                }
                            });
                    DirectoryInfoMessage dim = new DirectoryInfoMessage(files);
                    ctx.writeAndFlush(dim);
                    break;
                case "remove":
                    Path removeDir = Paths.get("./project/server_vault/" + request.getLogin() + "/" + request.getFilename());
                    if (Files.exists(removeDir)) {
                        removeDir.toFile().delete();
                    }
                    break;
                case "rename":
                    Path renameDir = Paths.get("./project/server_vault/" + request.getLogin() + "/" + request.getFilename());
                    Path newDir = Paths.get("./project/server_vault/" + request.getLogin() + "/" + request.getNewFileName());
                    if (Files.exists(renameDir)) {
                        renameDir.toFile().renameTo(newDir.toFile());
                    }
                    break;
            }
        }
        else ctx.fireChannelRead(msg);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("hi");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("bye");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }
}
