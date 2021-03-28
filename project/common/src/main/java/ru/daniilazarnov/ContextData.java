package ru.daniilazarnov;

import io.netty.buffer.ByteBuf;

import static io.netty.buffer.Unpooled.wrappedBuffer;

public class ContextData {
    private int command;
    private String filePath;
    private String login;
    private String password;
    private byte[] container;
    private final ByteBuf delimiter;
    private final byte [] delimArray;

    ContextData(){
        String del="%%%fucking_delimiter%%%";
        this.delimArray = del.getBytes();
        this.delimiter= wrappedBuffer(delimArray);
        this.filePath="X:\\emptyPath\\";
        this.login="empty";
        this.password="empty";
        this.container=new byte[1];
    }

    public void setCommand(int command) {
        this.command = command;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public void setContainer(byte[] container) {
        this.container = container;
    }

    public void clone (ContextData renew){
        this.command= renew.getCommand();
        this.login=renew.getLogin();
        this.password=renew.getPassword();
        this.filePath= renew.getFilePath();
        this.container=renew.getContainer();
    }


    public int getCommand() {
        return command;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public ByteBuf getDelimiter() {
        return delimiter;
    }

    public String getFilePath() {
        return filePath;
    }

    public byte[] getDelimArray() {
        return delimArray;
    }

    public byte[] getContainer() {
        return container;
    }
}
