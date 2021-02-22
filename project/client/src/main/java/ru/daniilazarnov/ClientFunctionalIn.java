package ru.daniilazarnov;

import io.netty.channel.ChannelHandlerContext;
import ru.daniilazarnov.operationWithFile.FileActions;
import ru.daniilazarnov.operationWithFile.OperationCommand;
import ru.daniilazarnov.operationWithFile.TypeOperation;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Scanner;

/*
    Данный класс отвечает за обработку поступающих с сервера данных
 */
public class ClientFunctionalIn {

    private String generalPath;
    private String clientName;

    protected void list(DataMsg dataMsg) {
        byte[] obj = dataMsg.getBytes();
        String paths = (String) ConvertToByte.deserialize(obj);
        String[] list = paths.split(";");
        for (String o : list) {
            o = o.replace(generalPath, "");
            if (!o.isBlank()) {
                System.out.println(o);
            }
        }
    }

    public static void fileDialog(Scanner scanner, String path, FileMsg fileMsg) throws IOException {
        boolean existsFile = FileMsg.checkExistsFile(path, fileMsg);
        if (existsFile) {
            System.out.println("File with this name exists, choose action:\n\t/wr - overwrite file\n\t/new - choose a new name (enter full path)");
            FileActions.actionsWithFile(TypeOperation.valueOf(scanner.nextLine()), fileMsg, path);
        } else {
            FileActions.actionsWithFile(TypeOperation.CREATE_UPDATE, fileMsg, path);
            Files.write(Path.of(path + fileMsg.getNameFile()), fileMsg.getBytes(), StandardOpenOption.CREATE);
            System.out.println("File uploaded successfully on path: " + path + fileMsg.getNameFile());
        }
    }

    protected void download(DataMsg dataMsg, Scanner scanner) {
        System.out.print("Enter the path where to save the file (Ex: C:/): ");
        String path = scanner.nextLine();
        FileMsg fileMsg = (FileMsg) ConvertToByte.deserialize(dataMsg.getBytes());
        if (fileMsg != null) {
            try {
                fileDialog(scanner, path, fileMsg);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("failed to load file");
        }
    }

    protected void upload(ChannelHandlerContext ctx, Scanner scanner) {

    }

    protected void remove(ChannelHandlerContext ctx, Scanner scanner) {
        System.out.print("Enter the path where to save the file: ");
        String path = scanner.nextLine();
        ctx.writeAndFlush(new DataMsg(Command.REMOVE, ConvertToByte.serialize(path)));
    }

    protected void move(ChannelHandlerContext ctx, Scanner scanner) {

    }

    protected void startClient(DataMsg msg) {
        clientName = (String) ConvertToByte.deserialize(msg.getBytes());
        generalPath = Path.of("project/server/directories/" + clientName).toString();
    }

    public void checkFileExistOnServer(ChannelHandlerContext ctx, Object msg) {
        DataMsg dataMsg = (DataMsg) msg;
        Scanner scanner = new Scanner(System.in);
        boolean isFileExist = (boolean) ConvertToByte.deserialize(dataMsg.getBytes());
        if (isFileExist) {
            System.out.println("a file with the same name already exists at the specified path, select an action: ");
            switch (scanner.nextLine()) {

            }
        }
    }

    private void dialogFileExist(boolean isFileExist, Scanner scanner) {
        if (isFileExist) {
            System.out.println("a file with the same name already exists at the specified path, select an action: ");
            OperationCommand.getList(TypeOperation.CREATE_UPDATE).forEach(x -> System.out.println(x.getCommand()));
            switch (scanner.nextLine()) {
                case "/wr":

            }
        }
    }
}
