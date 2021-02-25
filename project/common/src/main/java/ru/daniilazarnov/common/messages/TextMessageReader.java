package ru.daniilazarnov.common.messages;

import io.netty.buffer.ByteBuf;
import ru.daniilazarnov.common.CommonPackageConstants;
import java.nio.charset.StandardCharsets;


public class TextMessageReader {
    private TextMessageReadState state = TextMessageReadState.MESSAGE_LENGTH;

    private int messageLength;

    public void readMessage(ByteBuf buf) {
        if (state == TextMessageReadState.MESSAGE_LENGTH) {
            if (buf.readableBytes() >= CommonPackageConstants.CONTENT_LENGTH_BYTES.getCode()) {
                messageLength = buf.readInt();
                state = TextMessageReadState.MESSAGE;
            }
        }

        if (state == TextMessageReadState.MESSAGE) {
            if (buf.readableBytes() == messageLength) {
                byte[] messageBytes = new byte[messageLength];
                buf.readBytes(messageBytes);
                String message = new String(messageBytes, StandardCharsets.UTF_8);
                System.out.println(message);
                state = TextMessageReadState.COMPLETE;
            }
        }
    }

    public TextMessageReadState getState() {
        return state;
    }
}
