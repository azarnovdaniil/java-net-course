package ru.daniilazarnov.commands;

import java.nio.file.Path;

public final class UploadFile extends Commands {

    public UploadFile(Path path) {
    }

    @Override
    public boolean runCommands() {
        System.out.println("Реализовать загрузку файла на сервер");
        return true;
    }
}
