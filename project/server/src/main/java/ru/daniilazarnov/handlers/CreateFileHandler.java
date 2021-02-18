package ru.daniilazarnov.handlers;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

public class CreateFileHandler extends SimpleChannelInboundHandler<String> {

    private static final Logger LOG = LoggerFactory.getLogger(CreateFileHandler.class);
    private static final ConcurrentLinkedQueue<ChannelHandlerContext> clients
            = new ConcurrentLinkedQueue<>();

    private static int counter = 0;
    private String userName;
    private String path = "project/server/src/main/java/ru/daniilazarnov/exampleFiles";
    private String varPath = path;



    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        counter--;
        clients.remove(ctx);
        LOG.info("Client with name: {}, with ip: {} leave", userName, ctx.channel().localAddress());
    }
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        counter++;
        clients.add(ctx);
        userName = "user" + counter;
        LOG.info("Client with name: {}, with ip: {} connected", userName, ctx.channel().localAddress());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext context, String s) throws Exception {
        LOG.info(s);

        if (s.equals("--list\r\n")) {
            context.writeAndFlush("catalog\r\ncd\r\ntouch\r\nls\r\nmkdir\r\n");

        } else if (s.startsWith("catalog")) {
            String fileName = s.replace("catalog ", "");
            fileName = fileName.replace("\r\n", "");
            String catPath = varPath + "\\" + fileName;
            List<String> info = Files.readAllLines(Path.of(catPath));
            for (int i = 0; i < info.size(); i++) {
                info.set(i, info.get(i) + "\r\n");
                context.writeAndFlush(info.get(i));
            }
        } else if (s.startsWith("cd")) {
            String newPath = s.replace("cd ", "");
            newPath = newPath.replace("\r\n", "");
            if (newPath.equals("") || newPath.equals("cd")) {
                newPath = getVarPath() + "\r\n";
                context.writeAndFlush(newPath);
            } else {
                if (Files.isDirectory(Path.of(newPath(newPath).toString()), LinkOption.NOFOLLOW_LINKS)) {
                    Path directory = Paths.get(newPath(newPath).toString());
                    setVarPath(directory.toString());
                    String crPath = getVarPath() + "\r\n";
                    context.writeAndFlush(crPath);
                } else if (newPath.equals("/")){
                    setVarPath(path);
                    String crPath = path + "\r\n";
                    context.writeAndFlush(crPath);
                } else {
                    String errorDirectory = newPath + " is not directory" + "\r\n";
                    context.writeAndFlush(errorDirectory);
                }
            }

        } else if (s.startsWith("touch")) {
            String fileName = s.replace("touch ", "");
            fileName = fileName.replace("\r\n", "");
            Files.createFile(Path.of(varPath + "\\" + fileName));


        } else if (s.startsWith("ls")) {
            List<String> userFiles = getFiles();
            String lengthOfNumberFiles = String.valueOf(String.valueOf(userFiles.size()).length());
            LOG.info(lengthOfNumberFiles);
            context.write(lengthOfNumberFiles);
            context.flush();
            String numberFiles = String.valueOf(userFiles.size());
            LOG.info(numberFiles);
            context.write(numberFiles);
            context.flush();
            for (String userFile : userFiles) {
                String lengthOfLength = String.valueOf(String.valueOf(userFile.length()).length());
                LOG.info(lengthOfLength);
                context.write(lengthOfLength);

                String userFileLength = String.valueOf(userFile.length());
                LOG.info(userFileLength);
                context.write(userFileLength);

                LOG.info(userFile);
                context.write(userFile);
            }
            context.flush();

        } else if (s.startsWith("mkdir")) {
            String dirName = s.split(" +")[1];
            dirName = dirName.replace("\r\n", "");
            if (Files.notExists(Path.of(varPath, dirName))) {
                Files.createDirectory(Path.of(varPath, dirName));
            }
        } else if (s.startsWith("upload:")) {
            String fileName = s.replace("upload: ", "");
            fileName = fileName.replace("\r\n", "");
            LOG.info(fileName);

        } else {
            context.writeAndFlush("Unknown command!\r\n");
        }
    }
    public String getPath() {
        return path;
    }

    public String getVarPath() {
        return varPath;
    }

    public void setVarPath(String varPath) {
        this.varPath = varPath;
    }

    private Path newPath(String path){
        Path newPath = Path.of(getPath() + "\\" + path);
        return newPath;
    }

    private List<String> getFiles() throws IOException {
        return Files.list(Path.of(path)).map(path -> path.getFileName().toString())
                .collect(Collectors.toList());
    }
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
