package ru.daniilazarnov.console_IO;


import java.io.File;

public class OutputConsole {
    private static final int DELAY = 10;
    private static final String PROMPT_TO_ENTER = ">";
    private static final String PROGRAM_NAME = "local_storage ";
    private static final String USERNAME = "~" + File.separator + "user1";
    private static final String WELCOME_MESSAGE = "Добро пожаловать в файловое хранилище!\n"
            + "ver: 0.005a\n"
            + "uod: 17.02.2021\n";

    public static String welcomeMessageString() {
        return WELCOME_MESSAGE;
    }

    /**
     * Метод выводит на консоль строку приглашение ко вводу
     */
    public static void printPrompt() {
        try {
            Thread.sleep(DELAY);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.print(PROGRAM_NAME + USERNAME + PROMPT_TO_ENTER);
    }

}
