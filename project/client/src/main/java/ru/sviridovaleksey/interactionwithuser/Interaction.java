package ru.sviridovaleksey.interactionwithuser;

import io.netty.channel.Channel;
import ru.sviridovaleksey.workwithfile.WorkWithFileClient;

import java.util.Scanner;

public class Interaction {

    private final Scanner scanner = new Scanner(System.in);
    private final io.netty.channel.Channel channel;


public Interaction(Channel channel) {
    this.channel = channel;
}


    public void startInteraction(HelloMessage helloMessage, String userName, WorkWithFileClient workWithFileClient) {

        ChoseAction choseAction = new ChoseAction(channel, workWithFileClient);

        while (true) {
            helloMessage.helloMessage();
            String chose = scanner.next();
            choseAction.choseAction(chose, userName);
        }


    }


}
