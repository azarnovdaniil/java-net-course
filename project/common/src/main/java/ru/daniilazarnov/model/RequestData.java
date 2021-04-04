package ru.daniilazarnov.model;

public class RequestData {
    private Byte command;
    private Character separator;
    private Integer length;
    private String content;

    public Byte getCommand() {
        return command;
    }

    public void setCommand(Byte command) {
        this.command = command;
    }

    public Character getSeparator() {
        return separator;
    }

    public void setSeparator(Character separator) {
        this.separator = separator;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "RequestData{"
                + "command=" + command
                + ", separator=" + separator
                + ", length=" + length
                + ", content='" + content
                + '\'' + '}';
    }
}
