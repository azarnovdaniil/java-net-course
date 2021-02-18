package ru.daniilazarnov.commands;

import java.io.Serializable;
import java.nio.file.Path;


abstract public class Commands implements Serializable {

    public abstract boolean runCommands(Path userDir, String homeDir, String fileName, byte[] content, int segment, int allSegments);


}
