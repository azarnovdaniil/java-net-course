package ru.daniilazarnov;

public enum Command {

    /*
    ULF - uploadFile;
    DLF - downloadFile;
    FLS - files to list;
    RM_CLIENT - removeFileFromClientDirectory;
    RM_SERVER - removeFileFromServerDirectory;
    RNM_CLIENT - renameFileInClientDirectory;
    RNM_SERVER - renameFileInServerDirectory.
     */

    ULF ("ulf", (byte) 1),
    DLF ("dlf", (byte) 2),
    FLS ("fls", (byte) 3),
    RM_CLIENT("rmc", (byte) 4),
    RM_SERVER("rms", (byte) 5),
    RNM_CLIENT("rnms", (byte) 6),
    RNM_SERVER("rnms", (byte) 7);

    byte signal;
    String nameCommand;

    Command(String nameCommand, byte signal) {
        this.signal = signal;
        this.nameCommand = nameCommand;
    }

}
