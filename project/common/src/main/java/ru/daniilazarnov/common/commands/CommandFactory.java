package ru.daniilazarnov.common.commands;

import io.netty.channel.Channel;
import ru.daniilazarnov.common.commands.exit.ExitCommand;
import ru.daniilazarnov.common.commands.login.LoginCommand;
import ru.daniilazarnov.common.commands.download.DownloadCommand;
import ru.daniilazarnov.common.commands.show.ShowCommand;
import ru.daniilazarnov.common.commands.upload.UploadCommand;

public class CommandFactory {

    public Command createCommand(String inputString, Channel channel) throws IllegalArgumentException {
        int whitespaceIndex = inputString.indexOf(" ");
        if (whitespaceIndex != -1) {
            String command = inputString.substring(0, whitespaceIndex);
            String pathString = inputString.substring(whitespaceIndex + 1);
            System.out.println(command);
            if (command.equals(Commands.DOWNLOAD.getTitle())) {
                return new DownloadCommand(channel, pathString);
            }
            if (command.equals(Commands.UPLOAD.getTitle())) {
                return new UploadCommand(channel, pathString);
            }
            if (command.equals(Commands.LOGIN.getTitle())) {
                whitespaceIndex = pathString.indexOf(" ");
                if (whitespaceIndex != -1) {
                    return new LoginCommand(channel,
                            pathString.substring(0, whitespaceIndex),
                            pathString.substring(whitespaceIndex + 1));
                }
                throw new IllegalArgumentException("Incorrect syntax of the command: " + command);
            }
            throw new IllegalArgumentException("Unknown command: " + command);

        } else {
            if (inputString.equals(Commands.SHOW.getTitle())) {
                return new ShowCommand(channel);
            }
            if (inputString.equals(Commands.EXIT.getTitle())) {
                return new ExitCommand(channel);
            }
            throw new IllegalArgumentException("Unknown command:" + inputString);

        }
    }
}
