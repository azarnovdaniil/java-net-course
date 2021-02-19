package ru.daniilazarnov.console_IO;

import java.io.File;
import java.util.concurrent.TimeUnit;

public class OutputConsole {

    private static final int DELAY = 10;
    private static final String PROMPT_TO_ENTER = ">";
    private static final String PROGRAM_NAME = "local_storage ";
    private static boolean consoleBusy = false;
    private static final String WELCOME_MESSAGE = "Добро пожаловать в файловое хранилище!\n"
            + "ver: 0.005a\n"
            + "uod: 17.02.2021\n"
            + "****************************\n"
            + "* welcome to cloud storage *\n"
            + "****************************\n\n";
    private static String homeDirectory = "user1";

    public static boolean isConsoleBusy() {
        return consoleBusy;
    }

    public static void setConsoleBusy(boolean consoleBusy) {
        try {
            final int timeout = 10;
            TimeUnit.MILLISECONDS.sleep(timeout);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        OutputConsole.consoleBusy = consoleBusy;
    }

    public static String welcomeMessageString() {
        return WELCOME_MESSAGE;
    }

    /**
     * Метод выводит на консоль строку приглашение ко вводу
     */
    public static void printPrompt() {
        String username = "~" + File.separator + homeDirectory;
        try {
            Thread.sleep(DELAY);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.print(PROGRAM_NAME + username + PROMPT_TO_ENTER);
    }

}
