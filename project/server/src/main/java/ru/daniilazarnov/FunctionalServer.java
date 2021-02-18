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
    private final String GENERAL_PATH = "project/server/directories/";

    public FunctionalServer() {
    }

    private void list(final ChannelHandlerContext ctx, final String clientName) {
        try {
            Path path = Paths.get(GENERAL_PATH + clientName);
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

    private void downloadFile(ChannelHandlerContext ctx, Object msg) {
        String[] paths = getPaths((DataMsg) msg);
        try {
            RandomAccessFile file = new RandomAccessFile(paths[0], "r");
            byte[] bytes = new byte[(int) file.length()];
            file.readFully(bytes);
            FileMsg fileMsg = new FileMsg(getFileName(paths[0]), bytes);
            ctx.writeAndFlush(DataMsg.createMsg(Command.DOWNLOAD, fileMsg));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getFileName(String path) {
        File file = new File(path);
        return file.getName();
    }

    private String[] getPaths(DataMsg msg) {
        byte[] bytes = msg.getBytes();
        return (String[]) ConvertToByte.deserialize(bytes);
    }

    private void uploadFile() {
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

    public void executeCommand(ChannelHandlerContext ctx, Object msg, String clientName) {
        if (msg instanceof DataMsg) {
            Command command = ((DataMsg) msg).getCommand();
            switch (command) {
                case LIST:
                    list(ctx, clientName);
                    break;
                case DOWNLOAD:
                    downloadFile(ctx, msg);
                    break;
                case UPLOAD:
                    uploadFile();
                    break;
                case REMOVE:
                    removeFile(ctx, msg);
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

    private String[] splitLine(String cmd) {
        return cmd.replaceAll("[\\s]+", " ").split(" ");
    }

}
