package ru.sviridovaleksey.newclientconnection;

import org.apache.commons.lang3.StringUtils;
import ru.sviridovaleksey.Command;
import ru.sviridovaleksey.TypeCommand;
import ru.sviridovaleksey.commands.*;
import ru.sviridovaleksey.workwithfiles.ShowAllDirectory;
import ru.sviridovaleksey.workwithfiles.WorkWithFile;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;


public class WhatDo {

private final WorkWithFile workWithFile;
private final MessageForClient messageForClient;
private final String defAddress = "project/server/Storage/";
private final ShowAllDirectory showAllDirectory;
private final HashMap<String, String> whenClient = new HashMap<>();
private static final Logger LOGGER = Logger.getLogger(WhatDo.class.getName());


public WhatDo(WorkWithFile workWithFile, MessageForClient messageForClient, Handler fileHandler) {
LOGGER.addHandler(fileHandler);
showAllDirectory = new ShowAllDirectory(defAddress, fileHandler);
this.workWithFile = workWithFile;
this.messageForClient = messageForClient;
workWithFile.createDefaultDirectory(defAddress);

}

    protected void whatDo(Command command, String who) {

        if (command.getType().equals(TypeCommand.MESSAGE)) {
            MessageCommandData data = (MessageCommandData) command.getData();
            String message = data.getMessage();
            System.out.println(who + " " + message);

        } else if (command.getType().equals(TypeCommand.CREATE_NEW_DIRECTORY)) {
            CreateNewDirectory data = (CreateNewDirectory) command.getData();
            String directoryName = data.getDirectoryName();
            String userName = data.getUserName();
            workWithFile.createNewDirectory(userName, whenClient.get(userName) + directoryName);
            showDirectoryFoClient(showAllDirectory.startShowDirectory(whenClient.get(userName)).toString());


        } else if (command.getType().equals(TypeCommand.DELETE_DIRECTORY)) {
            DeleteDirectory data = (DeleteDirectory) command.getData();
            String directoryName = data.getDirectoryName();
            String userName = data.getUserName();
            workWithFile.deleteDirectory(whenClient.get(userName) + directoryName);
            showDirectoryFoClient(showAllDirectory.startShowDirectory(whenClient.get(userName)).toString());

        } else if (command.getType().equals(TypeCommand.DELETE_FILE)) {
            DeleteFile data = (DeleteFile) command.getData();
            String fileName = data.getFileName();
            String userName = data.getUserName();
            workWithFile.deleteFile(whenClient.get(userName) + fileName);
            showDirectoryFoClient(showAllDirectory.startShowDirectory(whenClient.get(userName)).toString());


        } else if (command.getType().equals(TypeCommand.CREATE_NEW_FILE)) {
            CreateNewFile data = (CreateNewFile) command.getData();
            String fileName = data.getFileName();
            String userName = data.getUserName();
            workWithFile.createNewFile(userName, whenClient.get(userName) + fileName);
            showDirectoryFoClient(showAllDirectory.startShowDirectory(whenClient.get(userName)).toString());

        } else if (command.getType().equals(TypeCommand.GET_SHOW_DIR)) {
            GetShowDir data = (GetShowDir) command.getData();
            String userName = data.getUserName();
            String wayToDir = data.getMessage();
            showDirectoryFoClient(showAllDirectory.startShowDirectory(whenClient.get(userName)
                + wayToDir).toString());
            whenClient.put(userName, whenClient.get(userName) + wayToDir + "/");

        } else if (command.getType().equals(TypeCommand.GET_BACK_DIR)) {
            GetBackDir data = (GetBackDir) command.getData();
            String userName = data.getUserName();
            String getNewLink = whenClient.get(userName);
            getNewLink = StringUtils.reverse(getNewLink);
            getNewLink = StringUtils.substringAfter(getNewLink, "/");
            String nameDir = StringUtils.substringBefore(getNewLink, "/");
            nameDir = StringUtils.reverse(nameDir);
            getNewLink = StringUtils.substringAfter(getNewLink, "/");
            getNewLink = StringUtils.reverse(getNewLink);
            if (nameDir.equals(userName)) {
                messageForClient.err("Дальше некуда");
                showDirectoryFoClient(showAllDirectory.startShowDirectory(whenClient.get(userName)).toString());
                return;
            }
            whenClient.put(userName, getNewLink + "/");
            showDirectoryFoClient(showAllDirectory.startShowDirectory(whenClient.get(userName)).toString());


        } else if (command.getType().equals(TypeCommand.WRITE_INTO_FILE)) {
            WriteInToFile data = (WriteInToFile) command.getData();
            String userName = data.getUserName();
            String fileName = data.getFileName();
            boolean endWrite = data.getEndWrite();
            byte[] dataForFile = data.getData();
            long cell = data.getCell();
            workWithFile.writeByteToFile(whenClient.get(userName) + "/" + fileName, dataForFile, cell);
            if (endWrite) {
                messageForClient.successfulAction("Передача файла " + fileName + " окончена");
                showDirectoryFoClient(showAllDirectory.startShowDirectory(whenClient.get(userName)).toString());
            }


        } else if (command.getType().equals(TypeCommand.REQUEST_FILE)) {
                RequestFileFromClient data = (RequestFileFromClient) command.getData();
                String userName = data.getUserName();
                String fileName = data.getFileName();
                sendFileForClient(userName, fileName);

        } else if (command.getType().equals(TypeCommand.RENAME_FILE)) {
                RenameFile data = (RenameFile) command.getData();
                String userName = data.getUserName();
                String oldName = data.getOldName();
                String newName = data.getNewName();
                if (workWithFile.renameFile(userName, whenClient.get(userName) + "/" + oldName,
                        whenClient.get(userName) + "/" + newName)) {
                    messageForClient.successfulAction("Файл " + oldName + " переименован в " + newName);
                } else {
                    messageForClient.err("Не удалось переименовать файл, возможно вы указали не существующий файл");
                }
                showDirectoryFoClient(showAllDirectory.startShowDirectory(whenClient.get(userName)).toString());
        }
    }

    private void sendFileForClient(String userName, String fileName) {

        File file = new File(whenClient.get(userName) + fileName);

        try {
            long cell = 0;
            final int step = 980000;
            int sendSize;
            boolean endWrite = false;
            RandomAccessFile raf = new RandomAccessFile(file, "r");
            raf.seek(cell);
            int length = (int) raf.length();

            while (length != 0) {
                if (length > step) {
                    sendSize = step;
                    length = length - step;
            } else {
            sendSize = length;
            length = 0;
            endWrite = true;
            }
                byte[] bt = new byte[sendSize];
                raf.read(bt);
                Command command = Command.writeInToFile(userName, file.getName(), bt, cell, endWrite);
                cell = cell + sendSize;
                messageForClient.sendCommandForClient(command);
            }
            raf.close();
            showDirectoryFoClient(showAllDirectory.startShowDirectory(whenClient.get(userName)).toString());
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, e.getMessage());
        }

    }

    protected void firstStep(String login) {
    whenClient.put(login, defAddress + login);
    workWithFile.createFirsDirectory(whenClient.get(login));
    }

    private void showDirectoryFoClient(String shad) {
        messageForClient.responseShowDirectory(shad);
    }

}
