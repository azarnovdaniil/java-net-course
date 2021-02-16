package ru.sviridovaleksey.network;

import ru.sviridovaleksey.Command;
import ru.sviridovaleksey.TypeCommand;
import ru.sviridovaleksey.commands.*;
import ru.sviridovaleksey.workwithfile.WorkWithFileClient;


public class WhatDoClient {

private  final String ansiReset = "\u001B[0m";
private  final String ansiRed = "\u001B[31m";
private  final String ansiBlue = "\u001B[34m";
private final WorkWithFileClient workWithFileClient = new WorkWithFileClient();
private final String defaultDirectoryForDownload = "project/client/Download/";

public WhatDoClient() {
    workWithFileClient.createDefaultDirectory(defaultDirectoryForDownload); }


    protected void whatDoClient(Command command) {

        if (command.getType().equals(TypeCommand.MESSAGE)) {
                MessageCommandData data = (MessageCommandData) command.getData();
                String message = data.getMessage();
                System.out.println("Ответ от сервера: " + message);

        } else if (command.getType().equals(TypeCommand.PING)) {
                System.out.println("Соединение установленно");

        } else if (command.getType().equals(TypeCommand.AUTH_ERROR)) {
                AuthErrorCommandData data = (AuthErrorCommandData) command.getData();
                String message = data.getErrorMessage();
                System.out.println(message);


        } else if (command.getType().equals(TypeCommand.ERR_ACTION_MESSAGE)) {
                ErrActionMessage data = (ErrActionMessage) command.getData();
                String message = data.getMessage();
                System.out.println(ansiRed + "Ответ от сервера: " + message + ansiReset);

        } else if (command.getType().equals(TypeCommand.SUCCESS_ACTION)) {
                SuccessAction data = (SuccessAction) command.getData();
                String message = data.getMessage();
                System.out.println(ansiBlue + "Ответ от сервера: " + message + ansiReset);


        } else if (command.getType().equals(TypeCommand.SHOW_ALL_IN_DIR)) {
                ShowAllInDirectory data = (ShowAllInDirectory) command.getData();
                String message = data.getMessage();
                System.out.println(message);

        } else if (command.getType().equals(TypeCommand.WRITE_INTO_FILE)) {
                WriteInToFile data = (WriteInToFile) command.getData();
                String fileName = data.getFileName();
                boolean endWrite = data.getEndWrite();
                byte[] dataForFile = data.getData();
                long cell = data.getCell();
                workWithFileClient.writeByteToFile(defaultDirectoryForDownload + fileName, dataForFile, cell);
                if (endWrite) {
                    System.out.println(ansiBlue + "Скачивание завершено, загляните в папку "
                            + defaultDirectoryForDownload + ansiReset);
            }
        }
    }

}
