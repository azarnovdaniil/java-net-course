package clientserver.commands;

import java.io.Serializable;

public class PrivateMessageCommandData implements Serializable {

    private static final long serialVersionUID = -8326L;
    private final String receiver;
    private final String message;

    public PrivateMessageCommandData(String receiver, String message) {
        this.receiver = receiver;
        this.message = message;
    }

    public String getReceiver() {
        return receiver;
    }

    public String getMessage() {
        return message;
    }
}
