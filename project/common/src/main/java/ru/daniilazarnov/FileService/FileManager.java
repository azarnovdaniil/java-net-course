package ru.daniilazarnov.FileService;

import ru.daniilazarnov.commands.Commands;

import java.nio.file.*;

public class FileManager {
    Commands command;
    Path userDir;
    String homeDir;
    String fileName;
    private byte[] content;
    int segment;
    int allSegments;

    public FileManager(Commands command, Path dir, String homeDir, String fileName, byte[] content, int segment,
                       int allSegments) {
        this.command = command;
        this.userDir = dir;
        this.fileName = fileName;
        this.content = content;
        this.segment = segment;
        this.allSegments = allSegments;
        this.homeDir = homeDir;
    }

    public boolean runner() {
        return command.runCommands(userDir, homeDir, fileName, content, segment, allSegments);
    }

}