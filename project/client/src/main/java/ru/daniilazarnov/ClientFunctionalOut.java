package ru.daniilazarnov;

import io.netty.channel.ChannelHandlerContext;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/*
    Этот класс отвечает за функционал на стороне клиента
*/

public class ClientFunctionalOut {

    protected void getInfo() {
        System.out.println("Command list: ");
        for (Command cmd : Command.values()) {
            if (!cmd.getCommand().equals("")) {
                System.out.println("\t" + cmd.getCommand() + cmd.getDescription());
            }
        }
    }

    protected String[] dialog(Scanner scanner, String... arg) {
        List<String> list = new ArrayList<>();
        for (String s : arg) {
            System.out.println(s);
            list.add(scanner.nextLine());
        }
        return list.toArray(String[]::new);
    }


    protected void upload(ChannelHandlerContext ctx, String[] filePath) {
        String path = filePath[0];
        boolean isFileExist = FileMsg.checkExistsFile(path);
        if (isFileExist) {
            try {
                RandomAccessFile file = new RandomAccessFile(path, "r");
                byte[] bytes = new byte[(int) file.length()];
                file.readFully(bytes);
                FileMsg fileMsg = new FileMsg(FileMsg.getFileName(path), bytes);
                ctx.writeAndFlush(DataMsg.createMsg(Command.UPLOAD, fileMsg));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("This file does not exist, or there was an error in the path, try again");
        }
    }

    public void checkExistOnServer(ChannelHandlerContext ctx, String[] filePath) {
        String nameFile = FileMsg.getFileName(filePath[0]);
        ctx.writeAndFlush(DataMsg.createMsg(Command.CHECK_FILE_EXIST, nameFile));
    }
}
