package ru.daniilazarnov.commands;

import java.nio.file.Path;

public final class DownloadFile extends Commands {

    public DownloadFile(Path path) {
    }

    @Override
    public boolean runCommands() {
        System.out.println("Реализовать скачивание файла");
        return true;
    }
}
