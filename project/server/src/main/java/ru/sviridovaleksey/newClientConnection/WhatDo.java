package ru.sviridovaleksey.newClientConnection;

import org.apache.commons.lang3.StringUtils;
import ru.sviridovaleksey.Command;
import ru.sviridovaleksey.commands.*;
import ru.sviridovaleksey.workwithfiles.ShowAllDirectory;
import ru.sviridovaleksey.workwithfiles.WorkWithFile;

import java.util.HashMap;


public class WhatDo {

private WorkWithFile workWithFile;
private MessageForClient messageForClient;
private final String defAddress = "project/server/Storage/";
private ShowAllDirectory showAllDirectory = new ShowAllDirectory(defAddress);
private HashMap<String, String> whenClient = new HashMap<String, String>();


public WhatDo(WorkWithFile workWithFile, MessageForClient messageForClient){
this.workWithFile = workWithFile;
this.messageForClient = messageForClient;
workWithFile.createDefaultDirectory(defAddress);

}

    protected void whatDo (Command command, String who) {
        switch (command.getType()) {
            case MESSAGE: {
                MessageCommandData data = (MessageCommandData) command.getData();
                String message = data.getMessage();
                System.out.println(who + " " + message);
                break;
            }

            case CREATE_NEW_DIRECTORY: {
                CreateNewDirectory data = (CreateNewDirectory) command.getData();
                String directoryName = data.getDirectoryName();
                String userName = data.getUserName();
                workWithFile.createNewDirectory(userName, whenClient.get(userName) + directoryName);
                showDirectoryFoClient(showAllDirectory.startShowDirectory(whenClient.get(userName)).toString());
                break;
            }
            case DELETE_DIRECTORY: {
                DeleteDirectory data = (DeleteDirectory) command.getData();
                String directoryName = data.getDirectoryName();
                String userName = data.getUserName();
                workWithFile.deleteDirectory(userName, whenClient.get(userName) + directoryName);
                showDirectoryFoClient(showAllDirectory.startShowDirectory(whenClient.get(userName)).toString());
                break;
            }

            case DELETE_FILE: {
                DeleteFile data = (DeleteFile) command.getData();
                String fileName = data.getFileName();
                String userName = data.getUserName();
                workWithFile.deleteFile(userName, whenClient.get(userName) + fileName);
                showDirectoryFoClient(showAllDirectory.startShowDirectory(whenClient.get(userName)).toString());
                break;
            }


            case CREATE_NEW_FILE: {
                CreateNewFile data = (CreateNewFile) command.getData();
                String fileName = data.getFileName();
                String userName = data.getUserName();
                workWithFile.createNewFile(userName, whenClient.get(userName) + fileName);
                showDirectoryFoClient(showAllDirectory.startShowDirectory(whenClient.get(userName)).toString());
                break;
            }

            case GET_SHOW_DIR: {
                GetShowDir data = (GetShowDir) command.getData();
                String userName = data.getUserName();
                String wayToDir = data.getMessage();
                showDirectoryFoClient(showAllDirectory.startShowDirectory(whenClient.get(userName)
                         + wayToDir).toString());
                whenClient.put(userName, whenClient.get(userName) + wayToDir + "/");
                break;

            }

            case GET_BACK_DIR: {
                GetBackDir data = (GetBackDir) command.getData();
                String userName = data.getUserName();
                String getNewLink = whenClient.get(userName);
                getNewLink = StringUtils.reverse(getNewLink);
                getNewLink = StringUtils.substringAfter(getNewLink, "/");
                String nameDir =  StringUtils.substringBefore(getNewLink, "/");
                nameDir = StringUtils.reverse(nameDir);
                getNewLink = StringUtils.substringAfter(getNewLink, "/");
                getNewLink = StringUtils.reverse(getNewLink);


                if (nameDir.equals(userName)) {
                    messageForClient.err("Дальше некуда");
                    showDirectoryFoClient(showAllDirectory.startShowDirectory(whenClient.get(userName)).toString());
                    break; }

                whenClient.put(userName, getNewLink + "/");
                showDirectoryFoClient(showAllDirectory.startShowDirectory(whenClient.get(userName)).toString());
                System.out.println(whenClient.get(userName));
                break;
            }
        }

    }

    protected void firstStep (String login) {
    whenClient.put(login ,defAddress+login);
    workWithFile.createFirsDirectory(whenClient.get(login));
    }

    private void showDirectoryFoClient (String shad) {
        System.out.println(shad);
        messageForClient.responseShowDirectory(shad);
    }

}
