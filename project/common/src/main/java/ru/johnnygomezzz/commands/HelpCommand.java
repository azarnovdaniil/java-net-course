package ru.johnnygomezzz.commands;

public class HelpCommand {

    public void printHelp() {
        System.out.println(Commands.HELP.getName() + " - " + Commands.HELP.getHelpValue() + "\n"
                + Commands.LS.getName() + " - " + Commands.LS.getHelpValue() + "\n"
                + Commands.TOUCH.getName() + " - " + Commands.TOUCH.getHelpValue() + "\n"
                + Commands.DELETE.getName() + " - " + Commands.DELETE.getHelpValue() + "\n"
                + Commands.DOWNLOAD.getName() + " - " + Commands.DOWNLOAD.getHelpValue() + "\n"
                + Commands.UPLOAD.getName() + " - " + Commands.UPLOAD.getHelpValue() + "\n"
                + Commands.QUIT.getName() + " - " + Commands.QUIT.getHelpValue() + "\n");
    }


}
