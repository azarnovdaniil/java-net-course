package ru.daniilazarnov;

import java.util.function.Consumer;

public class ClientPathHolder implements PathHolder {

    String pathToRepo;
    long fileLength;
    Consumer<String> print;


    ClientPathHolder(String pathToRepo, Consumer<String> print) {
        this.pathToRepo = pathToRepo;
        this.print = print;
    }

    @Override
    public void transComplete() {
        this.print.accept("File downloaded successfully!");
    }

    @Override
    public void setAuthority(String path) {
        this.pathToRepo = path;
    }

    @Override
    public String getAuthority() {
        return this.pathToRepo;
    }

    @Override
    public void setFileLength(long length) {
        this.fileLength = length;
    }

    @Override
    public long getFileLength() {
        return this.fileLength;
    }
}
