package ru.daniilazarnov.entity;

import java.nio.file.Path;

public class User {
    private String nickname;
    private String password;
    private Path path;
    private Path currentPath;

    public User(String nickname, String password, Path path) {
        this.nickname = nickname;
        this.password = password;
        this.path =path;
        this.currentPath =path;
    }

    public void setCurrentPath(Path currentPath) {
        this.currentPath = currentPath;
    }

    public Path getCurrentPath() {
        return currentPath;
    }

    public Path getPath() {
        return path;
    }

    public void setPath(Path path) {
        this.path = path;
    }

    public String getNickname() {
        return nickname;
    }

    public String getPassword() {
        return password;
    }
}