package ru.daniilazarnov;


import java.util.List;

public class DirectoryListInfo extends AbstractMsg {
    private List<String> filesAtDirectory;

    public DirectoryListInfo(List<String> filesAtDirectory) {
        this.filesAtDirectory = filesAtDirectory;
    }

    public List<String> getFilesAtDirectory() {
        return filesAtDirectory;
    }
}
