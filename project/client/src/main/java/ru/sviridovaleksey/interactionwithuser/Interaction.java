package ru.sviridovaleksey.interactionwithuser;

import io.netty.channel.Channel;
import ru.sviridovaleksey.Command;
import ru.sviridovaleksey.network.Connection;

import java.util.Scanner;

public class Interaction {

    private Scanner scanner = new Scanner(System.in);
    private ChoseAction choseAction;
    private io.netty.channel.Channel channel;
    private boolean authOk = false;




public Interaction(Channel channel) {
    this.channel = channel;
}


    public void startInteraction (HelloMessage helloMessage, String userName) {

        choseAction = new ChoseAction(scanner, channel);

        while (true) {
            helloMessage.helloMessage();
            String chose = scanner.next();
            choseAction.choseAction(chose, userName);
        }


    }


}
