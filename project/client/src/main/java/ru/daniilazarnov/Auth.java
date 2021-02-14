package ru.daniilazarnov;

import org.apache.log4j.Logger;

import java.io.*;
import java.util.Arrays;

import static ru.daniilazarnov.ServerNetworkMethods.*;

public class Auth {
    private static final Logger LOG = Logger.getLogger(ServerNetworkMethods.class);
    private static final int DELAY = 10;

    static boolean auth() {
        InputStream in = System.in;
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));

        String userName = "";
        char[] password = new char[0];

        while (isConnect()) {
            try {
                Thread.sleep(DELAY);
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

                System.out.println(userName + " - " + Arrays.toString(password));

            } catch (IOException e) {
                LOG.error(e);
            }
        } else {
            System.out.println("Enter username: ");
            userName = console.readLine("Username: ");
            password = console.readPassword("Password: ");
        }
        String passString = new String(password);
        sendStringAndCommandByte((userName + "%-%" + passString), Command.AUTH.getCommandByte());
        return false;
    }
}
