package ru.daniilazarnov;

import org.apache.log4j.Logger;

import java.io.*;

import static ru.daniilazarnov.NetworkCommunicationMethods.*;

public class Auth implements Constants {
    private static final Logger LOG = Logger.getLogger(NetworkCommunicationMethods.class);


    protected static String getStatusAuth() {
        return "Регистрация " + (ClientNetworkHandler.isAuth() ? "" : "не") + " подтверждена";
    }

    static boolean auth() {
        InputStream in = System.in;
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));

        String userName = "";
        char[] password = new char[0];

        while (isConnect()) {
            try {
                Thread.sleep(TEN);
            } catch (InterruptedException e) {
                LOG.error(e);
            }
        }

        Console console = System.console();
        if (console == null) {
            System.out.println("Логин: ");
            try {
                userName = bufferedReader.readLine().trim();
                System.out.println("Пароль: ");
                password = bufferedReader.readLine().trim().toCharArray();
            } catch (IOException e) {
                LOG.error(e);
            }
        } else {
            System.out.println("Enter username: ");
            userName = console.readLine("Username: ");
            password = console.readPassword("Password: ");
        }
        String passString = new String(password);
        ProgressBar.start();
        sendStringAndCommandByte((userName + "%-%" + passString), Command.AUTH.getCommandByte());
        return false;
    }

    @Override
    public void nothing() {

    }
}
