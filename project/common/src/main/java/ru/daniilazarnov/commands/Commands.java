package ru.daniilazarnov.commands;

import java.io.Serializable;
import java.nio.file.Path;


abstract public class Commands implements Serializable {
    private static DeleteFile DeleteFile;
    private static RenameFile RenameFile;
    private static DownloadFile DownloadFile;
    private static ShowFile ShowFile;
    private static UploadFile UploadFile;

    Commands(Path path, DeleteFile deleteFile) {
        DeleteFile dF=new DeleteFile(path);
    }
    Commands(Path path, RenameFile renameFile) {
        RenameFile rF = new RenameFile(path);
    }
    Commands(Path path, DownloadFile downloadFile) {
        RenameFile renameFile = new RenameFile(path);
    }
    Commands(Path path, ShowFile ShowFile) {
        RenameFile renameFile = new RenameFile(path);
    }

    Commands(Path path, UploadFile UploadFile) {
        RenameFile renameFile = new RenameFile(path);
    }

    public Commands() {
    }

    public boolean runCommands() {
        System.out.print("Выполняется команда:... ");
        return false;
    }
}
