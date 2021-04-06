package ru.daniilazarnov.clientCLI;

public class CheckCommand {
    public static boolean isAuth(String[] arguments) {
        if (arguments.length != 1) {
            System.out.println("Wrong command\n>");
            return false;
        }
        return true;
    }

    public static boolean isChangeDir(String[] arguments) {
        if (arguments.length != 1) {
            System.out.println("Wrong command\n>");
            return false;
        }
        return true;
    }

    public static boolean isConnect(String[] arguments) {
        if (arguments.length != 2) {
            System.out.println("Wrong command\n>");
            return false;
        }
        return true;
    }

    public static boolean isDisconnect(String[] arguments) {
        if (arguments.length != 0) {
            System.out.println("Wrong command\n>");
            return false;
        }
        return true;
    }

    public static boolean isMakeDir(String[] arguments) {
        if (arguments.length != 1) {
            System.out.println("Wrong command\n>");
            return false;
        }
        return true;
    }

    public static boolean isUlpoadFile(String[] arguments) {
        if (arguments.length != 1) {
            System.out.println("Wrong command\n>");
            return false;
        }
        return true;
    }

    public static boolean isDownloadFile(String[] arguments) {
        if (arguments.length != 1) {
            System.out.println("Wrong command\n>");
            return false;
        }
        return true;
    }

    public static boolean isPresentDir(String[] arguments) {
        if (arguments.length != 0) {
            System.out.println("Wrong command\n>");
            return false;
        }
        return true;
    }

    public static boolean isListOfFile(String[] arguments) {
        if (arguments.length != 0) {
            System.out.println("Wrong command\n>");
            return false;
        }
        return true;
    }

    public static boolean isShowHelp(String[] arguments) {
        if (arguments.length != 0) {
            System.out.println("Wrong command\n>");
            return false;
        }
        return true;
    }

}
