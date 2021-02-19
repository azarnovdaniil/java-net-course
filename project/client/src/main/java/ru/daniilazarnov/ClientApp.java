package ru.daniilazarnov;

import ru.daniilazarnov.auth.AuthClient;
import ru.daniilazarnov.console_IO.InputConsole;
import ru.daniilazarnov.console_IO.OutputConsole;

import java.io.IOException;

import static ru.daniilazarnov.network.NetworkCommunicationMethods.*;

public class ClientApp {
//    private static AuthClient authClient = new AuthClient();
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
