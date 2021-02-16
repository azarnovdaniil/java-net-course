package ru.daniilazarnov.commands;

import java.nio.file.Path;

public final class DeleteFile extends Commands {

    public DeleteFile(Path path) {
    }

    @Override
    public boolean runCommands() {
        System.out.println("Реализовать удаление файла");
        return true;
    }

}
