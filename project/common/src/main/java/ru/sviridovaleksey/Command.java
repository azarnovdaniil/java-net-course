package ru.sviridovaleksey;

import ru.sviridovaleksey.commands.*;

import java.io.Serializable;


public class Command implements Serializable {


    private TypeCommand type;
    private Object data;

    public TypeCommand getType() {
        return type;
    }

    public Object getData() {
        return data;
    }



    public static Command authCommand(String login, String password) {
        Command command = new Command();
        command.type = TypeCommand.AUTH;
        command.data = new AuthCommandData(login, password);
        return command;
    }

    public static Command authOkCommand(String username) {
        Command command = new Command();
        command.type = TypeCommand.AUTH_OK;
        command.data = new AuthOkCommandData(username);
        return command;
    }

    public static Command authErrorCommand(String authErrorMessage) {
        Command command = new Command();
        command.type = TypeCommand.AUTH_ERROR;
        command.data = new AuthErrorCommandData(authErrorMessage);
        return command;
    }

    public static Command message (String userName, String message) {
        Command command = new Command();
        command.type = TypeCommand.MESSAGE;
        command.data = new MessageCommandData(message, userName);
        return command;
    }

    public static Command createNewFile (String userName, String fileName) {
        Command command = new Command();
        command.type = TypeCommand.CREATE_NEW_FILE;
        command.data = new CreateNewFile(userName, fileName);
        return command;
    }

    public static Command createNewDirectory (String userName, String nameDirectory) {
        Command command = new Command();
        command.type = TypeCommand.CREATE_NEW_DIRECTORY;
        command.data = new CreateNewDirectory(userName, nameDirectory);
        return command;
    }

    public static Command deleteDirectory (String userName, String numberDirectory) {
        Command command = new Command();
        command.type = TypeCommand.DELETE_DIRECTORY;
        command.data = new DeleteDirectory(userName, numberDirectory);
        return command;
    }

    public static Command deleteFile (String userName, String numberFile) {
        Command command = new Command();
        command.type = TypeCommand.DELETE_FILE;
        command.data = new DeleteFile(userName, numberFile);
        return command;
    }

    public static Command copyFile (String userName, String numberFile) {
        Command command = new Command();
        command.type = TypeCommand.COPY_FILE;
        command.data = new CopyFile(userName, numberFile);
        return command;
    }

    public static Command pasteFile (String userName) {
        Command command = new Command();
        command.type = TypeCommand.PATE_FILE;
        command.data = new PasteFile(userName);
        return command;
    }

    public static Command ping () {
        Command command = new Command();
        command.type = TypeCommand.PING;
        return command;
    }

    public static Command errActionMessage (String userName, String message) {
        Command command = new Command();
        command.type = TypeCommand.ERR_ACTION_MESSAGE;
        command.data = new ErrActionMessage(message, userName);
        return command;
    }

    public static Command successAction (String userName, String message) {
        Command command = new Command();
        command.type = TypeCommand.SUCCESS_ACTION;
        command.data = new SuccessAction(message, userName);
        return command;
    }

    public static Command showAllInDirectory (String userName, String message) {
        Command command = new Command();
        command.type = TypeCommand.SHOW_ALL_IN_DIR;
        command.data = new ShowAllInDirectory(userName, message);
        return command;
    }

    public static Command getShowDir (String userName, String message) {
        Command command = new Command();
        command.type = TypeCommand.GET_SHOW_DIR;
        command.data = new GetShowDir(userName, message);
        return command;
    }

    public static Command getBackDir (String userName, String message) {
        Command command = new Command();
        command.type = TypeCommand.GET_BACK_DIR;
        command.data = new GetBackDir(userName, message);
        return command;
    }

    public static Command writeInToFile (String userName, String fileName, byte[] data, long cell, boolean endWrite) {
        Command command = new Command();
        command.type = TypeCommand.WRITE_INTO_FILE;
        command.data = new WriteInToFile(userName, fileName, data, cell, endWrite);
        return command;
    }

    public static Command requestFile (String userName, String fileName) {
        Command command = new Command();
        command.type = TypeCommand.REQUEST_FILE;
        command.data = new RequestFileFromClient(userName, fileName);
        return command;
    }

}

