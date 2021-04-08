package ru.daniilazarnov;

import ru.daniilazarnov.command.BaseCommand;
import ru.daniilazarnov.command.CommandRename;

import java.io.Serializable;

public class Command implements Serializable {
    private CommandType type;
    private Object data;

    public CommandType getType() {
        return type;
    }

    public Object getData() {
        return data;
    }

    public static Command commandCreateFile(String name) {
        Command command = new Command();
        command.type = CommandType.CREATE_NEW_FILE;
        command.data = new BaseCommand(name);
        return command;
    }

    public static Command commandCreateDir(String name) {
        Command command = new Command();
        command.type = CommandType.CREATE_NEW_DIR;
        command.data = new BaseCommand(name);
        return command;
    }

    public static Command commandRename(String oldName, String newName) {
        Command command = new Command();
        command.type = CommandType.RENAME;
        command.data = new CommandRename(oldName, newName);
        return command;
    }

    public static Command commandDelete(String name) {
        Command command = new Command();
        command.type = CommandType.DELETE;
        command.data = new BaseCommand(name);
        return command;
    }
}