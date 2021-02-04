package ru.sviridovaleksey.network;

import ru.sviridovaleksey.Command;
import ru.sviridovaleksey.commands.*;

import java.nio.channels.SelectionKey;

public class WhatDoClient {


    protected void whatDoClient (Command command) {

        switch (command.getType()) {
            case MESSAGE: {
                MessageCommandData data = (MessageCommandData) command.getData();
                String username = data.getUserName();
                String message = data.getMessage();
                System.out.println("Ответ от сервера: " + message);
                break;
            }

        }

    }

}
