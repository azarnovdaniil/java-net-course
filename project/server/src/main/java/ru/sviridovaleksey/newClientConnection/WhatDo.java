package ru.sviridovaleksey.newClientConnection;

import ru.sviridovaleksey.Command;
import ru.sviridovaleksey.commands.*;
import ru.sviridovaleksey.workwithfiles.WorkWithFile;

import java.nio.channels.SelectionKey;

public class WhatDo {

private WorkWithFile workWithFile;

public WhatDo(WorkWithFile workWithFile){

this.workWithFile = workWithFile;

}




    protected void whatDo (Command command, SelectionKey key) {
        String who = (String) key.attachment();
        switch (command.getType()) {
            case MESSAGE: {
                MessageCommandData data = (MessageCommandData) command.getData();
                String username = data.getUserName();
                String message = data.getMessage();
                System.out.println(who + " " + username + " " + message);
                break;
            }
            case AUTH: {
                AuthCommandData data = (AuthCommandData) command.getData();
                String login = data.getLogin();
                String password = data.getPassword();
                System.out.println(who + " " + login + " " + password);
                break;
            }
            case CREATE_NEW_DIRECTORY: {
                CreateNewDirectory data = (CreateNewDirectory) command.getData();
                String directoryName = data.getDirectoryName();
                String userName = data.getUserName();
                workWithFile.createNewDirectory(userName, directoryName);
                System.out.println(who + " " + userName + " " + directoryName);
                break;
            }
            case DELETE_DIRECTORY: {
                DeleteDirectory data = (DeleteDirectory) command.getData();
                String directoryName = data.getDirectoryName();
                String userName = data.getUserName();
                workWithFile.deleteDirectory(userName, directoryName);
                System.out.println(who + " " + userName + " " + directoryName);
                break;
            }

            case DELETE_FILE: {
                DeleteFile data = (DeleteFile) command.getData();
                String fileName = data.getFileName();
                String userName = data.getUserName();
                workWithFile.deleteFile(userName, fileName);
                System.out.println(who + " " + userName + " " + fileName);
                break;
            }


            case CREATE_NEW_FILE: {
                CreateNewFile data = (CreateNewFile) command.getData();
                String fileName = data.getFileName();
                String userName = data.getUserName();
                workWithFile.createNewFile(userName, fileName, "");
                System.out.println(who + " " + userName + " " + fileName);
                break;
            }
        }

    }
}
