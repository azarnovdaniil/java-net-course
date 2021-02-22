package ru.daniilazarnov.client.commands;

import io.netty.channel.Channel;
import ru.daniilazarnov.client.commands.login.LoginCommand;
import ru.daniilazarnov.common.commands.Command;
import ru.daniilazarnov.client.commands.download.DownloadCommand;
import ru.daniilazarnov.client.commands.show.ShowCommand;
import ru.daniilazarnov.client.commands.upload.UploadCommand;

public class CommandFactory {

    public Command createCommand(String inputString, Channel channel) throws IllegalArgumentException {
        int whitespaceIndex = inputString.indexOf(" ");
        if (whitespaceIndex != -1) {
            String command = inputString.substring(0, whitespaceIndex);
            String pathString = inputString.substring(whitespaceIndex + 1);
            System.out.println(command);
            if (command.equals(ClientCommand.DOWNLOAD.getTitle())) {
                return new DownloadCommand(channel, pathString);
            }
            if (command.equals(ClientCommand.UPLOAD.getTitle())) {
                return new UploadCommand(channel, pathString);
            }
            if (command.equals(ClientCommand.LOGIN.getTitle())) {
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
            if (inputString.equals(ClientCommand.SHOW.getTitle())) {
                return new ShowCommand(channel);
            }
            throw new IllegalArgumentException("Unknown command:" + inputString);

        }
    }
}
