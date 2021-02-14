package ru.daniilazarnov;

import java.io.Serializable;
import java.nio.ByteBuffer;

public enum Command implements Serializable {
    LIST("/list", " - get a list of all available files and directories", null),
    DOWNLOAD("/download", " - download file from server \n\t\t[Example: /download (path_to_file_server) (path_where_to_save_client)]", null),
    UPLOAD("/upload", " - send file to server \n\t\t[Example: /upload (path_to_file_client) (path_where_to_save_server)]", null),
    REMOVE("/remove", " - delete file on server \n\t\t[Example: /remove (path_to_file_server)]", null),
    MOVE("/move", " - moves the file to another directory \n\t\t[Example: /move (path_to_file_server) (path_where_to_move_server)]", null),
    CREATEDIR("/crdir", " - create directory on server \n\t\t[Example: /crdir (path_where_create_server)]", null),
    ERROR(""," - command from server was incorrect", null),
    START("", " - this command is needed at the start of client connection", null),
    EXIT("/exit", " - disconnect from server", null);

    private final String command;
    private String description;
    private byte[] bytes;

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

    public static Command createError(String description){
        Command command = Command.ERROR;
        command.setDescription(description);
        return command;
    }
}
