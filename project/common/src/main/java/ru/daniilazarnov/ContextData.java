package ru.daniilazarnov;

public class ContextData {
    private int command;
    private String fileName;

    public void setCommand(int command) {
        this.command = command;
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
