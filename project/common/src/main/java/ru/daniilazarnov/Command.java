package ru.daniilazarnov;

import java.io.Serializable;

public enum Command implements Serializable {
    LIST("/list", " - get a list of all available files and directories", null),
    DOWNLOAD("/download", " - download file from server", null),
    UPLOAD("/upload", " - send file to server", null),
    REMOVE("/remove", " - delete file on server", null),
    MOVE("/move", " - moves the file to another directory", null),
    CREATE_DIR("/crdir", " - create directory on server", null),
    ERROR("", " - command from server was incorrect", null),
    START("", " - this command is needed at the start of client connection", null),
    EXIT("/exit", " - disconnect from server", null);

    private final String command;
    private String description;
    private byte[] bytes;

    @SuppressWarnings("checkstyle:FinalParameters")
    Command(String command, String description, byte[] bytes) {
        this.command = command;
        this.description = description;
        this.bytes = bytes;
    }


    public String getCommand() {
        return command;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    public static Command createError(String description) {
        Command command = Command.ERROR;
        command.setDescription(description);
        return command;
    }


}
