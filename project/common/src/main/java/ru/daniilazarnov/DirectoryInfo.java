package ru.daniilazarnov;

import java.util.List;

public class DirectoryInfo extends AbstractMsg {
    private List<String> filesAtDirectory;

    public DirectoryInfo(List<String> filesAtDirectory) {
        this.filesAtDirectory = filesAtDirectory;
    }

    public List<String> getFilesAtDirectory() {
        return filesAtDirectory;
    }
}
