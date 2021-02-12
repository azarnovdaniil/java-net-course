package ru.sviridovaleksey.network;

import ru.sviridovaleksey.Command;
import ru.sviridovaleksey.commands.*;
import ru.sviridovaleksey.interactionwithuser.Interaction;

import java.nio.channels.SelectionKey;

public class WhatDoClient {

    private  final String ANSI_RESET = "\u001B[0m";
    private  final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_BLUE = "\u001B[34m";

    public WhatDoClient () {

    }


    protected void whatDoClient (Command command) {

        switch (command.getType()) {
            case MESSAGE: {
                MessageCommandData data = (MessageCommandData) command.getData();
                String username = data.getUserName();
                String message = data.getMessage();
                System.out.println("Ответ от сервера: " + message);
                break;
            }

            case PING: {
                System.out.println("Соединение установленно");
                break;
            }

            case AUTH_ERROR: {
                AuthErrorCommandData data = (AuthErrorCommandData) command.getData();
                String message = data.getErrorMessage();
                System.out.println(message);
                break;
            }

            case ERR_ACTION_MESSAGE: {
                ErrActionMessage data = (ErrActionMessage) command.getData();
                String message = data.getMessage();
                System.out.println(ANSI_RED + "Ответ от сервера: " + message + ANSI_RESET);
                break;
            }
            case SUCCESS_ACTION: {
                SuccessAction data = (SuccessAction) command.getData();
                String message = data.getMessage();
                System.out.println(ANSI_BLUE + "Ответ от сервера: " + message + ANSI_RESET);
                break;
            }

            case SHOW_ALL_IN_DIR: {
                ShowAllInDirectory data = (ShowAllInDirectory) command.getData();
                String message = data.getMessage();
                System.out.println(message);
                break;
            }


        }

    }

}
