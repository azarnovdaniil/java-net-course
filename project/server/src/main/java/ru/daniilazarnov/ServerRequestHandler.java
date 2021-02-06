package ru.daniilazarnov;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

public class ServerRequestHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof RequestMessage) {
            RequestMessage request = (RequestMessage) msg;
            String clientCmd = request.getCmd();

            /**
             * Действия сервера при получении запроса на скачивание файла
             */
            if (clientCmd.equals("download")) {
                if (Files.exists(Paths.get("C:\\Users\\rav05\\OneDrive\\Desktop\\java_course_final\\java-net-course\\project\\server\\src\\main\\java\\ru\\daniilazarnov\\server_vault\\" + request.getFilename()))) {
                    FileMessage fm = new FileMessage(Paths.get("C:\\Users\\rav05\\OneDrive\\Desktop\\java_course_final\\java-net-course\\project\\server\\src\\main\\java\\ru\\daniilazarnov\\server_vault\\" + request.getFilename()));
                    ctx.writeAndFlush(fm);
                }
            }
            else if (clientCmd.equals("show")) {
                List<String> files = new ArrayList<>();
                Files.walkFileTree(Paths.get("C:\\Users\\rav05\\OneDrive\\Desktop\\java_course_final\\java-net-course\\project\\server\\src\\main\\java\\ru\\daniilazarnov\\server_vault\\"),
                        new SimpleFileVisitor<Path>() {
                            @Override
                            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                                files.add(file.toString());
                                return FileVisitResult.CONTINUE;
                            }
                        });
                DirectoryInfoMessage dim = new DirectoryInfoMessage(files);
                ctx.writeAndFlush(dim);
            }
            else if (clientCmd.equals("remove")) {
                Path removeDir = Paths.get("C:\\Users\\rav05\\OneDrive\\Desktop\\java_course_final\\java-net-course\\project\\server\\src\\main\\java\\ru\\daniilazarnov\\server_vault\\" + request.getFilename());
                if (Files.exists(removeDir)) {
                    removeDir.toFile().delete();
                }
            }
            else if (clientCmd.equals("rename")) {
                Path renameDir = Paths.get("C:\\Users\\rav05\\OneDrive\\Desktop\\java_course_final\\java-net-course\\project\\server\\src\\main\\java\\ru\\daniilazarnov\\server_vault\\" + request.getFilename());
                Path newDir = Paths.get("C:\\Users\\rav05\\OneDrive\\Desktop\\java_course_final\\java-net-course\\project\\server\\src\\main\\java\\ru\\daniilazarnov\\server_vault\\" + request.getNewFileName());
                if (Files.exists(renameDir)) {
                    renameDir.toFile().renameTo(newDir.toFile());
                }
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
