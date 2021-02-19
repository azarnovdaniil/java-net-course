package ru.daniilazarnov;

import ru.daniilazarnov.console_IO.InputConsole;
import ru.daniilazarnov.console_IO.OutputConsole;

public class ClientApp {
    private static InputConsole input = new InputConsole();

    public static void main(String[] args) {
        System.out.print(OutputConsole.welcomeMessageString());
            input.inputConsoleHandler();
    }
}
