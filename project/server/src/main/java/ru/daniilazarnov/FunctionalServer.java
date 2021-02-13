package ru.daniilazarnov;

import io.netty.channel.ChannelHandlerContext;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FunctionalServer {
//    private final SelectionKey key;
//    private final User user;
    private final String separator = File.separator;
    public FunctionalServer() {
    }

    private void list(ChannelHandlerContext ctx, String clientName){
        try {
            //Stream<Path> list = Files.walk(Path.of(".."+ separator + "directories" + separator + clientName));
            //TODO
            Path path = Paths.get("project/server/directories/" + clientName);
            Stream<Path> list = Files.walk(path);
            List<Path> paths = list.collect(Collectors.toList());

            String stringPaths = "";
            for (Path o : paths) {
                stringPaths += o.toString() + ";";
            }
            DataMsg msg = new DataMsg(Command.LIST, ConvertToByte.serialize(stringPaths));
            //                cmdList.setBytes(ConvertToByte.serialize(stringPaths));
            ctx.writeAndFlush(msg);
        } catch (IOException e) {
            e.printStackTrace();
            Command error = Command.ERROR;
            error.setDescription("Error in generating user directory");
            ctx.writeAndFlush(error);
        }
    }

    private void downloadFile(){

    }

    private void uploadFile(){

    }

    private void removeFile(){

    }

    public void executeCommand(ChannelHandlerContext ctx, Object msg, String clientName){
        if (msg instanceof Command){
            Command command = (Command) msg;
            switch (command){
                case LIST:
                    list(ctx, clientName);
                    break;
                case DOWNLOAD:
                    ctx.writeAndFlush(Command.DOWNLOAD);
                    //downloadFile();
                    break;
                case UPLOAD:
                    uploadFile();
                    break;
                case REMOVE:
                    removeFile();
                    break;
                case EXIT:
                    ctx.channel().close();
                    break;
            }
        } else if (msg instanceof FileMsg){

        } else {
            System.out.println("Incorrect message");
        }
    }
}
