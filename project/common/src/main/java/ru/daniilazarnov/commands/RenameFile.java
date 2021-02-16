package ru.daniilazarnov.commands;

import java.nio.file.Path;

public final class RenameFile extends Commands{

    public RenameFile(Path path) {
    }

    @Override
    public boolean runCommands() {
        System.out.println("Реализовать переименование файла на сервере");
        return true;
    }
}
