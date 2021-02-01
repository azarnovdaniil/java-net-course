package clientserver;

import clientserver.commands.*;

import java.io.Serializable;
import java.util.List;

public class Command implements Serializable {
    private static final long serialVersionUID = -60L;

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

    public static Command publicMessageCommand(String username, String message) {
        Command command = new Command();
        command.type = CommandType.PUBLIC_MESSAGE;
        command.data = new PublicMessageCommandData(username, message);
        return command;
    }

    public static Command privateMessageCommand(String receiver, String message) {
        Command command = new Command();
        command.type = CommandType.PRIVATE_MESSAGE;
        command.data = new PrivateMessageCommandData(receiver, message);
        return command;
    }

    public static Command updateUsersListCommand(List<String> users) {
        Command command = new Command();
        command.type = CommandType.UPDATE_USERS_LIST;
        command.data = new UpdateUsersListCommandData(users);
        return command;
    }

    public static Command endCommand() {
        Command command = new Command();
        command.type = CommandType.END;
        return command;
    }

    public static Command changeLoginCommand(String login, String loginNew) {
        Command command = new Command();
        command.type = CommandType.CHANGE_LOGIN;
        command.data = new ChangeLoginCommandData(login, loginNew);
        return command;
    }

    public static Command changeLoginOkCommand(String loginNew) {
        Command command = new Command();
        command.type = CommandType.CHANGE_LOGIN_OK;
        command.data = new ChangeLoginOKCommandData(loginNew);
        return command;
    }

    public static Command changeLoginErrCommand(String message) {
        Command command = new Command();
        command.type = CommandType.CHANGE_LOGIN_ERR;
        command.data = new ChangeLoginErrCommandData(message);
        return command;
    }
}
