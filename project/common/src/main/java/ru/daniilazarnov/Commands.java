package ru.daniilazarnov;

import ru.daniilazarnov.CommandsType.*;
import java.io.Serializable;
import java.util.List;

public class Commands implements Serializable {

    private CommandsList type;
    private Object data;

    public CommandsList getType() {
        return type;
    }

    public Object getData() {
        return data;
    }

    public static Commands authCommand(String login, String password) {
        Commands command = new Commands();
        command.type = CommandsList.AUTH;
        command.data = new AuthCommandData(login, password);
        return command;
    }


    public static Commands authOkCommand(String username) {
        Commands command = new Commands();
        command.type = CommandsList.AUTH_OK;
        command.data = new AuthOkCommandData(username);
        return command;
    }

    public static Commands authErrorCommand(String authErrorMessage) {
        Commands command = new Commands();
        command.type = CommandsList.AUTH_ERROR;
        command.data = new AuthErrorCommandData(authErrorMessage);
        return command;
    }

    public static Commands helpInfoCommand(String message, String sender) {
        Commands command = new Commands();
        command.type = CommandsList.HELP_MESSAGE;
        command.data = new HelpInfoCommandData(message, sender);
        return command;
    }


//    public static Commands showFilesListCommand(List<String> users) {
//        Commands command = new Commands();
//        command.type = CommandsList.SHOW_FILES_LIST;
//        command.data = new ShowFilesListCommandData(users);
//        return command;
//    }
//
//    public static Commands uploadFilesCommand(List<String> users) {
//        Commands command = new Commands();
//        command.type = CommandsList.UPLOAD;
//        command.data = new UploadFilesCommandData(users);
//        return command;
//    }
//
//    public static Commands downloadFilesCommand(List<String> users) {
//        Commands command = new Commands();
//        command.type = CommandsList.DOWNLOAD;
//        command.data = new DownloadFilesCommandData(users);
//        return command;
//    }
//
//    public static Commands renameFilesCommand(List<String> users) {
//        Commands command = new Commands();
//        command.type = CommandsList.RENAME_FILES;
//        command.data = new RenameFilesCommandData(users);
//        return command;
//    }
//
//        public static Commands deleteFilesCommand(List<String> users) {
//        Commands command = new Commands();
//        command.type = CommandsList.DELETE_FILES;
//        command.data = new DeleteFilesCommandData(users);
//        return command;
//    }

    public static Commands endCommand() {
        Commands command = new Commands();
        command.type = CommandsList.END;
        return command;
    }

}
