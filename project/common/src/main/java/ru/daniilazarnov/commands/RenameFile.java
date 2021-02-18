package ru.daniilazarnov.commands;

import java.nio.file.Path;

public final class RenameFile extends Commands{

    @Override
    public boolean runCommands(Path userDir, String homeDir, String fileName, byte[] content, int segment, int allSegments) {
        return  false;
    }
}
