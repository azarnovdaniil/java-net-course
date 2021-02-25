package ru.daniilazarnov;

import io.netty.channel.ChannelHandlerContext;
import ru.daniilazarnov.operationWithFile.FileActions;
import ru.daniilazarnov.operationWithFile.TypeOperation;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
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
            errorMsg(ctx, "Error in generating user directory");
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
            errorMsg(ctx, "Failed to download file from server");
        }
    }


    private String[] getPaths(DataMsg msg) {
        byte[] bytes = msg.getBytes();
        return (String[]) ConvertToByte.deserialize(bytes);
    }

    private void uploadFile(ChannelHandlerContext ctx, Object msg, String generalPath) {
        DataMsg data = (DataMsg) msg;
        FileMsg fileMsg = (FileMsg) ConvertToByte.deserialize(data.getBytes());
        String path = generalPath + File.separator + fileMsg.getNameFile();
        try {
            FileActions.actionsWithFile(TypeOperation.CREATE_UPDATE, fileMsg, path);
            ctx.writeAndFlush(DataMsg.createMsg(Command.UPLOAD, "File upload to server completed successfully"));
        } catch (IOException e) {
            errorMsg(ctx, "Error uploading file to server");
        }
    }

    private void removeFile(ChannelHandlerContext ctx, Object msg) {
        DataMsg data = (DataMsg) msg;
        String path = (String) ConvertToByte.deserialize(data.getBytes());
        if (Files.exists(Path.of(path))) {
            try {
                FileActions.actionsWithFile(TypeOperation.DELETE, null, path);
                ctx.writeAndFlush(DataMsg.createMsg(Command.REMOVE, "File deleted successfully"));
            } catch (IOException e) {
                errorMsg(ctx, "Failed to give file");
            }
        } else {
            errorMsg(ctx, "Incorrect path to file, try again");
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
                case NEW_NAME:
                    rename(ctx, msg, generalPath);
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

    private void rename(ChannelHandlerContext ctx, Object msg, String generalPath) {
        DataMsg data = (DataMsg) msg;
        String[] dataPaths = (String[]) ConvertToByte.deserialize(data.getBytes());
        if (isCheckCorrectDataPath(dataPaths)) {
            getCorrectPath(dataPaths, generalPath);
            try {
                FileActions.actionsWithFile(TypeOperation.RENAME, null, dataPaths);
                ctx.writeAndFlush(DataMsg.createMsg(Command.NEW_NAME, "Rename file successful"));
            } catch (IOException e) {
                errorMsg(ctx, "Failed to rename file");
            }
        } else {
            errorMsg(ctx, "One of the specified paths is empty");
        }
    }

    private void getCorrectPath(String[] dataPaths, String generalPath) {
        for (int i = 0; i < dataPaths.length; i++) {
            dataPaths[i] = generalPath + File.separator + dataPaths[i];
        }
    }

    private boolean isCheckCorrectDataPath(String[] paths) {
        return Stream.of(paths).anyMatch(String::isBlank);
    }

    private boolean isCheckFileExist(ChannelHandlerContext ctx, Object msg, String generalPath) {
        DataMsg dataMsg = (DataMsg) msg;
        String path = generalPath + ConvertToByte.deserialize(dataMsg.getBytes());
        return FileMsg.checkExistsFile(path);
    }

    private String[] splitLine(String cmd) {
        return cmd.replaceAll("[\\s]+", " ").split(" ");
    }

    private void errorMsg(ChannelHandlerContext ctx, String err) {
        Command error = Command.ERROR;
        error.setDescription(err);
        ctx.writeAndFlush(new DataMsg(error, null));
    }
}
