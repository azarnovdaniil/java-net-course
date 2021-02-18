package ru.daniilazarnov;

import java.util.Scanner;

public class ClientCommands {
    private static final byte COMMAND_LOGIN = 15;
    private static final byte EMPTY = -1;
    private static final byte COMMAND_VIEW = 10;
    private static final byte COMMAND_UPLOAD = 33;
    private static final byte COMMAND_DOWNLOAD = 37;
    private static final byte COMMAND_DELETE = 66;
    private static final byte NOT_FOUND = -19;
    private static final byte NAME_NOT_FOUND = -17;
    private static final byte LOG_OUT = 23;


    public static byte getCommandLogin() {
        return COMMAND_LOGIN;
    }

    public static byte getEmpty() {
        return EMPTY;
    }

    public static byte getCommandView() {
        return COMMAND_VIEW;
    }

    public static byte getCommandUpload() {
        return COMMAND_UPLOAD;
    }

    public static byte getCommandDownload() {
        return COMMAND_DOWNLOAD;
    }

    public static byte getCommandDelete() {
        return COMMAND_DELETE;
    }

    public static byte getNotFound() {
        return NOT_FOUND;
    }

    public static byte getNameNotFound() {
        return NAME_NOT_FOUND;
    }

    public static byte getLogOut() {
        return LOG_OUT;
    }

    private static final Scanner scanner = new Scanner(System.in);

    public static void printMessage(String message) {
        System.out.println(message);
    }

    public static int getMenuItems() {
        while (true) {
            try {
                int i;
                String buff = scanner.nextLine();
                i = Integer.parseInt(buff);
                if (i >= 0 && i < 5) {
                    return i;
                } else {
                    printMessage(" Выберите команду :");
                }
            } catch (NumberFormatException e) {
                printMessage(" Выберите команду :");
            }
        }
    }

    public static int choiceBetweenThree() {
        while (true) {
            try {
                int i;
                String buff = scanner.nextLine();
                i = Integer.parseInt(buff);
                if (i >= 0 && i < 3) {
                    return i;
                } else {
                    printMessage(" Выберите команду :");
                }
            } catch (NumberFormatException e) {
                printMessage(" Выберите команду :");
            }
        }
    }

    public static String getText() {
        return scanner.nextLine();
    }
}
