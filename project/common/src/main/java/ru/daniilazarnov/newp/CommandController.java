package ru.daniilazarnov.newp;

import io.netty.channel.Channel;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class CommandController {
    private Channel channel;

    public CommandController(Channel channel) {
        this.channel = channel;
    }

    public void command(String[] input) throws IOException {
        String cmd = input[0].toUpperCase();
        switch (cmd) {
            case "LS":
                channel.writeAndFlush(new RequestLS());
                break;
            case "UPL": {
                String path = input[1];
                String fileName = input[2];
                channel.writeAndFlush(new FileMessage(fileName, Files.readAllBytes(Path.of(path + "\\" + fileName))));
                break;
            }
            case "DOWN": {
                String fileName = input[1];
                channel.writeAndFlush(new FileRequestMessage(fileName));
                break;
            }
        }
    }
}
