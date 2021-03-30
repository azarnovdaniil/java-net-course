package ru.daniilazarnov;

public enum CommandsToServer {
    stor((byte) 1),
    mkd((byte) 2),
    cd((byte) 3),
    retr((byte) 4),
    user((byte) 5),
    connect((byte) 6),
    help((byte) 7);

    private final byte numberOfCommand;

    CommandsToServer(byte numberOfCommand) {
        this.numberOfCommand = numberOfCommand;
    }

    public byte getNumberOfCommand() {
        return numberOfCommand;
    }


}
