package ru.daniilazarnov.commands;

import java.nio.file.Path;

public final class ShowFile extends Commands {

    public ShowFile(Path path) {
    }

    @Override
    public boolean runCommands() {

        System.out.println("Реализовать показ файлов на сервере");
        return true;
    }
}
