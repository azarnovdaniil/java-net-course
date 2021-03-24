package ru.daniilazarnov;

import io.netty.buffer.ByteBuf;

import static io.netty.buffer.Unpooled.wrappedBuffer;

public class ContextData {
    private int command;
    private String filePath;
    private String login;
    private String password;
    private ByteBuf delimiter;

    ContextData(){
        String del="%%%fucking_delimiter%%%";
        this.delimiter= wrappedBuffer(del.getBytes());
        this.filePath="X:\\emptyPath\\";
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

    public void clone (ContextData renew){
        this.command= renew.getCommand();
        this.login=renew.getLogin();
        this.password=renew.getPassword();
        this.filePath= renew.getFilePath();
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
}
