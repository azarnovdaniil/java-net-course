package ru.daniilazarnov.operationWithFile;

import ru.daniilazarnov.Command;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/*
    This enum
    type:
        1 - update or create file
 */
public enum OperationCommand {
    OVERWRITE("/wr", " - overwrite existing file", TypeOperation.CREATE_UPDATE),
    NEW_NAME("/new", "- create file with new name", TypeOperation.CREATE_UPDATE);

    private String command;
    private String description;
    private final TypeOperation type;

    OperationCommand(String command, String description, TypeOperation type) {
        this.command = command;
        this.description = description;
        this.type = type;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TypeOperation getType() {
        return type;
    }

    public static List<OperationCommand> getList(TypeOperation type) {
        return Stream.of(OperationCommand.values()).filter(x -> x.getType() == type).collect(Collectors.toList());
    }
}
