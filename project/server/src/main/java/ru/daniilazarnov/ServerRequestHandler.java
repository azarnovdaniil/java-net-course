package ru.daniilazarnov;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
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
                    if (Files.exists(Paths.get("./project/server/src/main/java/ru/daniilazarnov/server_vault/" + request.getLogin() + "/" + request.getFilename()))) {
                        FileMessage fm = new FileMessage(Paths.get("./project/server/src/main/java/ru/daniilazarnov/server_vault/" + request.getLogin() + "/" + request.getFilename()), request.getLogin());
                        ctx.writeAndFlush(fm);
                    }
                    break;
                case "list":
                    List<String> files = new ArrayList<>();
                    Files.walkFileTree(Paths.get("./project/server/src/main/java/ru/daniilazarnov/server_vault/" + request.getLogin()),
                            new SimpleFileVisitor<Path>() {
                                @Override
                                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                                    files.add(file.toString());
                                    return FileVisitResult.CONTINUE;
                                }
                            });
                    DirectoryInfoMessage dim = new DirectoryInfoMessage(files);
                    ctx.writeAndFlush(dim);
                    break;
                case "remove":
                    Path removeDir = Paths.get("./project/server/src/main/java/ru/daniilazarnov/server_vault/" + request.getLogin() + "/" + request.getFilename());
                    if (Files.exists(removeDir)) {
                        removeDir.toFile().delete();
                    }
                    break;
                case "rename":
                    Path renameDir = Paths.get("./project/server/src/main/java/ru/daniilazarnov/server_vault/" + request.getLogin() + "/" + request.getFilename());
                    Path newDir = Paths.get("./project/server/src/main/java/ru/daniilazarnov/server_vault/" + request.getLogin() + "/" + request.getNewFileName());
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
