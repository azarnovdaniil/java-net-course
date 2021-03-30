package ru.daniilazarnov;

import java.nio.file.Path;

public class UserInfo {
    private String name;
    private Path currentPath;
    private Path rootPath;

    public UserInfo(String name) {
        this.name = name;
    }
    public void setCurrentPath(Path currentPath) {
        this.currentPath = currentPath;
    }
    public Path getCurrentPath() {
        return currentPath;
    }
    public void setRootPath(Path rootPath) {
        this.rootPath = rootPath;
    }
    public Path getUserRoot() {
        return rootPath;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
}
