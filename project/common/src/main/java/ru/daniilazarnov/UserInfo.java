package ru.daniilazarnov;

import java.nio.file.Path;
import java.nio.file.Paths;

public class UserInfo {
    private String name;
    private Path currentPath;



    public void setCurrentPath(Path currentPath) {
        this.currentPath = currentPath;
    }
    public Path getCurrentPath(){
        return currentPath;
    }


    public Path getUserRoot(){
        return Paths.get(name);
    }

    public UserInfo(String name) {
        this.name = name;
    }



}
