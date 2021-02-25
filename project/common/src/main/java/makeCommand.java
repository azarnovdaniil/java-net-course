package common;

import common.Services.*;

public class makeCommand
{
    private CommandTypes type;
    private Object data;

    public CommandTypes getType() {
        return type;
    }

    public Object getData() {
        return data;
    }

    public static makeCommand authCommand(String login, String password) {
        makeCommand command = new makeCommand();
        command.type = CommandTypes.AUTH;
        command.data = new AuthCommandService(login, password);
        return command;
    }

    public static makeCommand authOkCommand(String username) {
        makeCommand command = new makeCommand();
        command.type = CommandTypes.AUTH_OK;
        command.data = new AuthOkCommandService(username);
        return command;
    }

    public static makeCommand authErrorCommand(String authErrorMessage) {
        makeCommand command = new makeCommand();
        command.type = CommandTypes.AUTH_ERROR;
        command.data = new AuthErrorCommandService(authErrorMessage);
        return command;
    }

    public static makeCommand message (String userName, String message) {
        makeCommand command = new makeCommand();
        command.type = CommandTypes.MESSAGE;
        command.data = new MessageCommandService(userName, message);
        return command;
    }
}
