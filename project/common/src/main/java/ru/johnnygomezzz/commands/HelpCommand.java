package ru.johnnygomezzz.commands;

public class HelpCommand {

    public void printHelp() {
        System.out.println("Команды локального хранилища:\n"
                + Commands.LS.getName() + " - " + Commands.LS.getHelpValue() + "\n"
                + Commands.TOUCH.getName() + " - " + Commands.TOUCH.getHelpValue() + "\n"
                + Commands.DELETE.getName() + " - " + Commands.DELETE.getHelpValue() + "\n"
                + Commands.DOWNLOAD.getName() + " - " + Commands.DOWNLOAD.getHelpValue() + "\n"
                + Commands.UPLOAD.getName() + " - " + Commands.UPLOAD.getHelpValue() + "\n"
                + Commands.MKDIR.getName() + " - " + Commands.MKDIR.getHelpValue() + "\n"
                + Commands.RENAME.getName() + " - " + Commands.RENAME.getHelpValue() + "\n"
                + Commands.QUIT.getName() + " - " + Commands.QUIT.getHelpValue() + "\n"
        + "\nКоманды серверного хранилища:\n"
                + Commands.SLS.getName() + " - " + Commands.SLS.getHelpValue() + "\n"
                + Commands.STOUCH.getName() + " - " + Commands.STOUCH.getHelpValue() + "\n"
                + Commands.SDELETE.getName() + " - " + Commands.SDELETE.getHelpValue() + "\n"
                + Commands.SRENAME.getName() + " - " + Commands.SRENAME.getHelpValue() + "\n"
                + Commands.SMKDIR.getName() + " - " + Commands.SMKDIR.getHelpValue() + "\n");
    }
}
