package messages;

import java.io.Serializable;

public class Message implements Serializable {

    private MessageType type;
    private Object msg;

    public Message(MessageType type, Object message) {
        this.type = type;
        this.msg = message;
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public Object getMessage() {
        return msg;
    }

    public void setMessage(Object message) {
        this.msg = message;
    }
}
