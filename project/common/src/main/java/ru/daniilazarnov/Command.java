package ru.daniilazarnov;
import ru.daniilazarnov.commands.*;

import java.io.Serializable;

public class Command implements Serializable {

    private CommandType type;
    private Object data;

    public CommandType getType() {
        return type;
    }

    public Object getData() {
        return data;
    }

    public static Command authCommand(String login, String password) {
        Command command = new Command();
        command.type = CommandType.AUTH;
        command.data = new AuthCommandData(login, password);
        return command;
    }

    public static Command authOkCommand(String username) {
        Command command = new Command();
        command.type = CommandType.AUTH_OK;
        command.data = new AuthOkCommandData(username);
        return command;
    }

    public static Command authErrorCommand(String authErrorMessage) {
        Command command = new Command();
        command.type = CommandType.AUTH_ERROR;
        command.data = new AuthErrorCommandData(authErrorMessage);
        return command;
    }

    public static Command requestFileCommand(String username, String filename) {
        Command command = new Command();
        command.type = CommandType.REQUEST_FILE;
        command.data = new RequestFileData(username, filename);
        return command;
    }

    public static Command sendFileCommand(String username, String filename, byte[] arr) {
        Command command = new Command();
        command.type = CommandType.SEND_FILE;
        command.data = new SendFileData(username, filename, arr);
        return command;
    }

    public static Command removeFileCommand(String username, String filename) {
        Command command = new Command();
        command.type = CommandType.REMOVE_FILE;
        command.data = new RemoveFileData(username, filename);
        return command;
    }

    public static Command renameFileCommand(String username, String filenameOld, String filenameNew) {
        Command command = new Command();
        command.type = CommandType.RENAME_FILE;
        command.data = new RenameFileData(username, filenameOld, filenameNew);
        return command;
    }

    public static Command errorCommand(String errorMessage) {
        Command command = new Command();
        command.type = CommandType.ERROR;
        command.data = new ErrorCommandData(errorMessage);
        return command;
    }

    public static Command messageInfoCommand(String message, String sender) {
        Command command = new Command();
        command.type = CommandType.INFO_MESSAGE;
        command.data = new MessageInfoCommandData(message, sender);
        return command;
    }

    public static Command endCommand() {
        Command command = new Command();
        command.type = CommandType.END;
        return command;
    }

    public static Command getDirCommand(String username) {
        Command command = new Command();
        command.type = CommandType.GET_DIR;
        command.data = new GetDirCommandData(username);
        return command;
    }

}
