package ru.daniilazarnov.client;

import io.netty.channel.Channel;
import ru.daniilazarnov.client.commands.CommandFactory;
import ru.daniilazarnov.common.commands.Command;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Client {

    private final CommandFactory commandFactory;
    private final Channel channel;

    public Client(Channel channel) {
        this.channel = channel;
        commandFactory = new CommandFactory();
    }

    public void run() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String inputString;
        while ((inputString = br.readLine()) != null) {
            Command command = commandFactory.createCommand(inputString, channel);
            command.execute();
        }
    }



}
