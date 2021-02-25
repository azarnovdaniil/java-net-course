package ru.daniilazarnov;

import io.netty.channel.ChannelHandlerContext;
import ru.daniilazarnov.operationWithFile.FileActions;
import ru.daniilazarnov.operationWithFile.OperationCommand;
import ru.daniilazarnov.operationWithFile.TypeOperation;

import java.io.IOException;
import java.nio.file.Path;
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
            TypeOperation type = TypeOperation.valueOf(scanner.nextLine());
            if (type == TypeOperation.RENAME) {
                System.out.print("Enter the path where to save with new file name: ");
                fileDialog(scanner, scanner.nextLine(), fileMsg);
            } else {
                FileActions.actionsWithFile(TypeOperation.valueOf(scanner.nextLine()), fileMsg, path);
            }
        } else {
            FileActions.actionsWithFile(TypeOperation.CREATE_UPDATE, fileMsg, path);
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

    protected void remove(ChannelHandlerContext ctx, DataMsg msg) {
        System.out.println(ConvertToByte.deserialize(msg.getBytes()));
    }

    protected void move(ChannelHandlerContext ctx, Scanner scanner) {

    }

    protected void startClient(DataMsg msg) {
        clientName = (String) ConvertToByte.deserialize(msg.getBytes());
        generalPath = Path.of("project/server/directories/" + clientName).toString();
    }

    public boolean checkFileExistOnServer(ChannelHandlerContext ctx, Object msg) {
        DataMsg dataMsg = (DataMsg) msg;
        Scanner scanner = new Scanner(System.in);
        return (Boolean) ConvertToByte.deserialize(dataMsg.getBytes());
//        if (isFileExist) {
//            System.out.println("a file with the same name already exists at the specified path, select an action: ");
//            OperationCommand.printList(TypeOperation.CREATE_UPDATE);
//            switch (scanner.nextLine()) {
//
//            }
//        }
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
