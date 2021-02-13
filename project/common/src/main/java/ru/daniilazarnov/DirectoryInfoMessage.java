package ru.daniilazarnov;

import java.util.List;

public class DirectoryInfoMessage extends AbstractMessage {
    private List<String> filesAtDirectory;

    public DirectoryInfoMessage(List<String> filesAtDirectory) {
        this.filesAtDirectory = filesAtDirectory;
    }

    public List<String> getFilesAtDirectory() {
        return filesAtDirectory;
    }
}
