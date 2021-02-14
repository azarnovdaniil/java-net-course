package ru.daniilazarnov;

import io.netty.channel.ChannelHandlerContext;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Scanner;

public class ClientFunctionalIn {

    protected void list(DataMsg dataMsg){
        byte[] obj = dataMsg.getBytes();
        String paths = (String) ConvertToByte.deserialize(obj);
        String[] list = paths.split(";");
        for (String o : list) {
            System.out.println(o);
        }
    }

    protected void downloadFile(DataMsg dataMsg, Scanner scanner) {
        System.out.print("Enter the path where to save the file: ");
        String path = scanner.nextLine();
        FileMsg fileMsg = (FileMsg) ConvertToByte.deserialize(dataMsg.getBytes());
        if (fileMsg != null) {
            try {
                checkExistsFile(scanner, path, fileMsg);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("failed to load file");
        }
    }

    private void checkExistsFile(Scanner scanner, String path, FileMsg fileMsg) throws IOException {
        if (Files.exists(Path.of(path + fileMsg.getNameFile()))) {
            System.out.println("File with this name exists, choose action:\n\t/wr - overwrite file\n\t/new - choose a new name");
            switch (scanner.nextLine()) {
                case "wr":
                    Files.write(Path.of(path + fileMsg.getNameFile()), fileMsg.getBytes(), StandardOpenOption.CREATE);
                    System.out.println("File uploaded successfully on path: " + path + fileMsg.getNameFile());
                    break;
                case "new":
                    System.out.print("Enter a new filename: ");
                    String newFileName = scanner.nextLine();
                    fileMsg.setNameFile(newFileName);
                    checkExistsFile(scanner, path, fileMsg);
            }
        } else {
            Files.write(Path.of(path + fileMsg.getNameFile()), fileMsg.getBytes(), StandardOpenOption.CREATE);
            System.out.println("File uploaded successfully on path: " + path + fileMsg.getNameFile());
        }
    }

    protected void uploadFile(ChannelHandlerContext ctx, Scanner scanner) {

    }

    protected void removeFile(ChannelHandlerContext ctx, Scanner scanner) {

    }

    protected void moveFile(ChannelHandlerContext ctx, Scanner scanner){

    }

}
