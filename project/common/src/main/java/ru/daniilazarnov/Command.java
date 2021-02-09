package ru.daniilazarnov;

public enum Command {
    DOWNLOAD((byte) 2),
    UPLOAD((byte) 1),
    CONNECT((byte) 3),
    LS((byte) 4),
    STATUS((byte) 5),
    EXIT((byte) 0);

    byte commandByte;

    Command(byte commandByte) {
        this.commandByte = commandByte;
    }
}
