package ru.daniilazarnov;

import io.netty.channel.ChannelHandlerContext;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FunctionalServer {

    public FunctionalServer() {
    }

    private void list(ChannelHandlerContext ctx, String generalPath) {
        try {
            Path path = Paths.get(generalPath);
            Stream<Path> list = Files.walk(path);
            List<Path> paths = list.collect(Collectors.toList());

            String stringPaths = "";
            for (Path o : paths) {
                stringPaths += o.toString() + ";";
            }
            DataMsg msg = new DataMsg(Command.LIST, ConvertToByte.serialize(stringPaths));
            ctx.writeAndFlush(msg);
        } catch (IOException e) {
            e.printStackTrace();
            Command error = Command.ERROR;
            error.setDescription("Error in generating user directory");
            ctx.writeAndFlush(error);
        }
    }

    private void downloadFile(ChannelHandlerContext ctx, Object msg, String generalPath) {
        String[] paths = getPaths((DataMsg) msg);
        try {
            RandomAccessFile file = new RandomAccessFile(generalPath + "/" + paths[0], "r");
            byte[] bytes = new byte[(int) file.length()];
            file.readFully(bytes);
            FileMsg fileMsg = new FileMsg(FileMsg.getFileName(paths[0]), bytes);
            ctx.writeAndFlush(DataMsg.createMsg(Command.DOWNLOAD, fileMsg));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private String[] getPaths(DataMsg msg) {
        byte[] bytes = msg.getBytes();
        return (String[]) ConvertToByte.deserialize(bytes);
    }

    private void uploadFile(ChannelHandlerContext ctx, Object msg, String generalPath) {
        DataMsg data = (DataMsg) msg;
        FileMsg fileMsg = (FileMsg) ConvertToByte.deserialize(data.getBytes());

    }

    private void removeFile(ChannelHandlerContext ctx, Object msg) {
        DataMsg data = (DataMsg) msg;
        String path = (String) ConvertToByte.deserialize(data.getBytes());
        if (Files.exists(Path.of(path))) {
            try {
                Files.delete(Path.of(path));
            } catch (IOException e) {
                Command error = Command.ERROR;
                error.setDescription("Failed to give file");
                ctx.writeAndFlush(new DataMsg(error, null));
            }
        } else {
            Command error = Command.ERROR;
            error.setDescription("Incorrect path to file, try again");
            ctx.writeAndFlush(new DataMsg(error, null));
        }
    }

    public void executeCommand(ChannelHandlerContext ctx, Object msg, String generalPath) {
        if (msg instanceof DataMsg) {
            Command command = ((DataMsg) msg).getCommand();
            switch (command) {
                case LIST:
                    list(ctx, generalPath);
                    break;
                case DOWNLOAD:
                    downloadFile(ctx, msg, generalPath);
                    break;
                case UPLOAD:
                    uploadFile(ctx, msg, generalPath);
                    break;
                case REMOVE:
                    removeFile(ctx, msg);
                    break;
                case CHECK_FILE_EXIST:
                    checkFileExist(ctx, msg, generalPath);
                    break;
                case EXIT:
                    ctx.channel().close();
                    break;
            }
        } else if (msg instanceof FileMsg) {

        } else {
            System.out.println("Incorrect message");
        }
    }

    private void checkFileExist(ChannelHandlerContext ctx, Object msg, String generalPath) {
        DataMsg dataMsg = (DataMsg) msg;
        String path = generalPath + ConvertToByte.deserialize(dataMsg.getBytes());
        boolean isFileExist = FileMsg.checkExistsFile(path);
        ctx.writeAndFlush(DataMsg.createMsg(Command.CHECK_FILE_EXIST, isFileExist));
    }

    private String[] splitLine(String cmd) {
        return cmd.replaceAll("[\\s]+", " ").split(" ");
    }

}
