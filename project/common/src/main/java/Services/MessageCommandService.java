package common.Services;

import java.io.Serializable;

public class MessageCommandService implements Serializable
{
    private final String userName;
    private final String message;

    public MessageCommandService(String message, String userName) {
        this.userName = userName;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public String getUserName() {
        return userName;
    }
}
