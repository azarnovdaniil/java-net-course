package ru.daniilazarnov.clientConnection;

import java.io.IOException;
import java.nio.file.Path;

public interface IClientConnection {
    boolean connect();
    boolean disconnect();
    boolean listOfFileDirectory();
    boolean presentWorkDirectory();
    boolean showHelp();
    boolean authorization(String userName, String password);
    boolean changeDirectory(Path targetDir);
    boolean downloadFile(String fileName);
    boolean makeDirectory(String dirName);
    boolean uploadFile(Path pathToFile);
}
