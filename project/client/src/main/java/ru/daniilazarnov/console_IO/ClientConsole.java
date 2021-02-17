package ru.daniilazarnov.console_IO;

import ru.daniilazarnov.network.ClientNetworkHandler;

import java.io.IOException;

import static ru.daniilazarnov.network.NetworkCommunicationMethods.auth;
import static ru.daniilazarnov.network.NetworkCommunicationMethods.init;

public class ClientConsole {
    private static InputConsole input = new InputConsole();

    public static void main(String[] args) throws IOException {
        init();
        while (!ClientNetworkHandler.isAuth()) {

            auth();
            if (ClientNetworkHandler.isAuth()) {
                init();
            } else {
                break;
            }
        }
        System.out.print(OutputConsole.welcomeMessageString());
        input.inputConsoleHandler();
    }
}
