package ru.sviridovaleksey.newclientconnection;


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

private final String separator = "\\";
private final WorkWithFile workWithFile;
private final MessageForClient messageForClient;
private final String defaultAddress;
private final ShowAllDirectory showAllDirectory;
private final HashMap<String, String> whereClient = new HashMap<>();
private final Logger logger = Logger.getLogger(WhatDo.class.getName());


public WhatDo(WorkWithFile workWithFile, MessageForClient messageForClient, Handler fileHandler, String defAddress) {
logger.addHandler(fileHandler);
this.defaultAddress = defAddress + separator + "Storage" + separator;
showAllDirectory = new ShowAllDirectory(this.defaultAddress, fileHandler);
this.workWithFile = workWithFile;
this.messageForClient = messageForClient;
workWithFile.createDefaultDirectory(this.defaultAddress);

}

    protected void whatDo(Command command, String who) {


        if (command.getType().equals(TypeCommand.MESSAGE)) {
            MessageCommandData data = (MessageCommandData) command.getData();
            String message = data.getMessage();
            System.out.println(who + " " + message);

        } else if (command.getType().equals(TypeCommand.CREATE_NEW_DIRECTORY)) {
            String message;
            CreateNewDirectory data = (CreateNewDirectory) command.getData();
            String directoryName = data.getDirectoryName();
            String userName = data.getUserName();
            message = workWithFile.createNewDirectory(userName, whereClient.get(userName) + directoryName);
            messageForClient.message(message);
            showDirectoryFoClient(showAllDirectory.startShowDirectory(whereClient.get(userName)).toString());


        } else if (command.getType().equals(TypeCommand.DELETE_DIRECTORY)) {
            String message;
            DeleteDirectory data = (DeleteDirectory) command.getData();
            String directoryName = data.getDirectoryName();
            String userName = data.getUserName();
            message = workWithFile.deleteDirectory(whereClient.get(userName) + directoryName);
            messageForClient.message(message);
            showDirectoryFoClient(showAllDirectory.startShowDirectory(whereClient.get(userName)).toString());

        } else if (command.getType().equals(TypeCommand.DELETE_FILE)) {
            String message;
            DeleteFile data = (DeleteFile) command.getData();
            String fileName = data.getFileName();
            String userName = data.getUserName();
            message = workWithFile.deleteFile(whereClient.get(userName) + fileName);
            messageForClient.message(message);
            showDirectoryFoClient(showAllDirectory.startShowDirectory(whereClient.get(userName)).toString());


        } else if (command.getType().equals(TypeCommand.CREATE_NEW_FILE)) {
            String message;
            CreateNewFile data = (CreateNewFile) command.getData();
            String fileName = data.getFileName();
            String userName = data.getUserName();
            message = workWithFile.createNewFile(userName, whereClient.get(userName) + fileName);
            messageForClient.message(message);
            showDirectoryFoClient(showAllDirectory.startShowDirectory(whereClient.get(userName)).toString());

        } else if (command.getType().equals(TypeCommand.GET_SHOW_DIR)) {
            GetShowDir data = (GetShowDir) command.getData();
            String userName = data.getUserName();
            String wayToDir = data.getMessage();
            showDirectoryFoClient(showAllDirectory.startShowDirectory(whereClient.get(userName)
                + wayToDir).toString());
            whereClient.put(userName, whereClient.get(userName) + wayToDir + separator);

        } else if (command.getType().equals(TypeCommand.GET_BACK_DIR)) {
            GetBackDir data = (GetBackDir) command.getData();
            String userName = data.getUserName();
            String nameDir = new File(whereClient.get(userName)).getName();
            if (nameDir.equals(userName)) {
                messageForClient.err("Дальше некуда");
                showDirectoryFoClient(showAllDirectory.startShowDirectory(whereClient.get(userName)).toString());
                return;
            }
            String getNewLink = new File(whereClient.get(userName)).getParent();
            whereClient.put(userName, getNewLink + separator);
            showDirectoryFoClient(showAllDirectory.startShowDirectory(whereClient.get(userName)).toString());


        } else if (command.getType().equals(TypeCommand.WRITE_INTO_FILE)) {
            WriteInToFile data = (WriteInToFile) command.getData();
            String userName = data.getUserName();
            String fileName = data.getFileName();
            boolean endWrite = data.getEndWrite();
            byte[] dataForFile = data.getData();
            long cell = data.getCell();
            workWithFile.writeByteToFile(whereClient.get(userName) + separator + fileName, dataForFile, cell);
            if (endWrite) {
                messageForClient.successfulAction("Передача файла " + fileName + " окончена");
                showDirectoryFoClient(showAllDirectory.startShowDirectory(whereClient.get(userName)).toString());
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
                if (workWithFile.renameFile(userName, whereClient.get(userName) + separator + oldName,
                        whereClient.get(userName) + separator + newName)) {
                    messageForClient.successfulAction("Файл " + oldName + " переименован в " + newName);
                } else {
                    messageForClient.err("Не удалось переименовать файл, возможно вы указали не существующий файл");
                }
                showDirectoryFoClient(showAllDirectory.startShowDirectory(whereClient.get(userName)).toString());
        }
    }

    private void sendFileForClient(String userName, String fileName) {

        File file = new File(whereClient.get(userName) + fileName);

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
            showDirectoryFoClient(showAllDirectory.startShowDirectory(whereClient.get(userName)).toString());
        } catch (IOException e) {
            logger.log(Level.SEVERE, e.getMessage());
        }

    }

    protected void firstStep(String login) {
    whereClient.put(login, defaultAddress + login);
    workWithFile.createFirsDirectory(whereClient.get(login));
    }

    private void showDirectoryFoClient(String shad) {
        messageForClient.responseShowDirectory(shad);
    }

}
