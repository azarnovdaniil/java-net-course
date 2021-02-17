package ru.daniilazarnov.console_IO;

import ru.daniilazarnov.auth.AuthClient;

import java.io.IOException;

import static ru.daniilazarnov.network.NetworkCommunicationMethods.*;

public class ConsoleClient {
    private static AuthClient authClient = new AuthClient();
    private static InputConsole input = new InputConsole();

    public static void main(String[] args) throws IOException {
        init();
        if (!AuthClient.isAuthStatus()) {
            authClient.auth();
            if (AuthClient.isAuthStatus()) {
                System.out.print(OutputConsole.welcomeMessageString());
                input.inputConsoleHandler();
            } else {
                exit();
            }
        }
    }
}
