package ru.daniilazarnov.command;

import ru.daniilazarnov.Command;

public class CommandHandler {
    Command command;
    StorageCommand baseCom = new StorageCommand();

    public void handler(Object msg) {
        if (msg instanceof Command) {
            command = (Command) msg;
            System.out.println(command.getType()); // логирование
        }

        switch (command.getType()) {
            case CREATE_NEW_FILE: {
                BaseCommand crFile = (BaseCommand) command.getData();
                baseCom.createFile(crFile.getName());
                break;
            }
            case CREATE_NEW_DIR: {
                BaseCommand crDir = (BaseCommand) command.getData();
                baseCom.createDir(crDir.getName());
                break;
            }
            case RENAME: {
                CommandRename ren = (CommandRename) command.getData();
                baseCom.rename(ren.getOldName(), ren.getNewName());
                break;
            }
            case DELETE: {
                BaseCommand del = (BaseCommand) command.getData();
                baseCom.delete(del.getName());
                break;
            }
        }
    }
}