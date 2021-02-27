package ru.daniilazarnov.old;

import io.netty.channel.Channel;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class CommandControllerOld {
    private Channel channel;

    public CommandControllerOld(Channel channel) {
        this.channel = channel;
    }

    public void command(String[] input) throws IOException {
        String cmd = input[0].toUpperCase();
        switch (cmd) {
            case "LS":
                channel.writeAndFlush(new RequestLSOld());
                break;
            case "UPL": {
                String path = input[1];
                String fileName = input[2];
                channel.writeAndFlush(new FileMessageOld(fileName, Files.readAllBytes(Path.of(path + "\\" + fileName))));
                break;
            }
            case "DOWN": {
                String fileName = input[1];
                channel.writeAndFlush(new FileRequestMessageOld(fileName));
                break;
            }
        }
    }
}
