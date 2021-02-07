package ru.daniilazarnov;

public enum Command {
    /*
    UF - Upload file;
    DF - Download file;
    AF - All files;
    */

    UF ("UF", (byte) 1),
    DF ("DF", (byte) 2),
    AF ("AF", (byte) 3);

    byte signal;
    String nameCommand;

    Command(String nameCommand, byte signal) {
        this.signal = signal;
        this.nameCommand = nameCommand;
    }
}
