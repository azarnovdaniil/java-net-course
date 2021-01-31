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

}