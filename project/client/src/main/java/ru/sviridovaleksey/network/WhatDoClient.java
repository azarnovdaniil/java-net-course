package ru.sviridovaleksey.network;

import ru.sviridovaleksey.Command;
import ru.sviridovaleksey.commands.*;
import ru.sviridovaleksey.workwithfile.WorkWithFileClient;


public class WhatDoClient {

    private  final String ANSI_RESET = "\u001B[0m";
    private  final String ANSI_RED = "\u001B[31m";
    private  final String ANSI_BLUE = "\u001B[34m";
    private final WorkWithFileClient workWithFileClient = new WorkWithFileClient();
    private final String defaultDirectoryForDownload = "project/client/Download/" ;

    public WhatDoClient () {
        workWithFileClient.createDefaultDirectory(defaultDirectoryForDownload);
    }


    protected void whatDoClient (Command command) {

        switch (command.getType()) {
            case MESSAGE: {
                MessageCommandData data = (MessageCommandData) command.getData();
                String message = data.getMessage();
                System.out.println("Ответ от сервера: " + message);
                break;
            }

            case PING: {
                System.out.println("Соединение установленно");
                break;
            }

            case AUTH_ERROR: {
                AuthErrorCommandData data = (AuthErrorCommandData) command.getData();
                String message = data.getErrorMessage();
                System.out.println(message);
                break;
            }

            case ERR_ACTION_MESSAGE: {
                ErrActionMessage data = (ErrActionMessage) command.getData();
                String message = data.getMessage();
                System.out.println(ANSI_RED + "Ответ от сервера: " + message + ANSI_RESET);
                break;
            }
            case SUCCESS_ACTION: {
                SuccessAction data = (SuccessAction) command.getData();
                String message = data.getMessage();
                System.out.println(ANSI_BLUE + "Ответ от сервера: " + message + ANSI_RESET);
                break;
            }

            case SHOW_ALL_IN_DIR: {
                ShowAllInDirectory data = (ShowAllInDirectory) command.getData();
                String message = data.getMessage();
                System.out.println(message);
                break;
            }

            case WRITE_INTO_FILE: {
                WriteInToFile data = (WriteInToFile) command.getData();
                String fileName = data.getFileName();
                boolean endWrite = data.getEndWrite();
                byte[] dataForFile = data.getData();
                long cell = data.getCell();
                workWithFileClient.writeByteToFile(defaultDirectoryForDownload + fileName,dataForFile,cell);
                if (endWrite) {
                    System.out.println(ANSI_BLUE + "Скачивание завершено, загляните в папку "
                            + defaultDirectoryForDownload + ANSI_RESET);
                }
                break;
            }


        }

    }

}
