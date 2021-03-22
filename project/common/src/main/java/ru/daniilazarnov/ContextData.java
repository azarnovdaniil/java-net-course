package ru.daniilazarnov;

import io.netty.buffer.ByteBuf;

import static io.netty.buffer.Unpooled.wrappedBuffer;

public class ContextData {
    private int command;
    private String fileName;
    private ByteBuf delimiter;

    ContextData(){
        String del="%%%fucking_delimiter%%%";
        delimiter= wrappedBuffer(del.getBytes());
    }

    public void setCommand(int command) {
        this.command = command;
    }

    public ByteBuf getDelimiter() {
        return delimiter;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getCommand() {
        return command;
    }

    public String getFileName() {
        return fileName;
    }
}
