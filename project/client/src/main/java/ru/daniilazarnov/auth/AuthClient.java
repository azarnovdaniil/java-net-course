package ru.daniilazarnov.auth;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import org.apache.log4j.Logger;
import ru.daniilazarnov.Command;
import ru.daniilazarnov.ProgressBar;
import ru.daniilazarnov.ReceivingAndSendingStrings;

import java.io.*;

import static ru.daniilazarnov.network.NetworkCommunicationMethods.*;
import static ru.daniilazarnov.constants.Constants.*;


public class AuthClient {
    private static final Logger LOG = Logger.getLogger(AuthClient.class);
    private static boolean authStatus = false;

    public static boolean isAuthStatus() {
        return authStatus;
    }

    public void setAuthStatus(boolean authStatus) {
        AuthClient.authStatus = authStatus;
    }

    public void authentication(ByteBuf buf, ChannelHandlerContext ctx) {
        String right = ReceivingAndSendingStrings.receiveAndEncodeString(buf);
        if (right.equals("1")) {
            setAuthStatus(true);
            LOG.debug("Access to the remote base received");

        } else {
            setAuthStatus(false);
            LOG.info("You do not have access to the remote database\n"
                    + "connection broken");
            ctx.close();
            System.exit(0);
        }

    }


    public String getStringStatusAuth() {
        return "Регистрация " + (isAuthStatus() ? "" : "не") + " подтверждена";
    }

    public static void auth() {


        while (isConnect()) {
            try {
                Thread.sleep(TEN);
            } catch (InterruptedException e) {
                LOG.error(e);
            }
        }

        sendStringAndCommandByte(inputLoginAndPassword(), Command.AUTH.getCommandByte());
        ProgressBar.start(FIVE);
    }

    private static String inputLoginAndPassword() {
        InputStream in = System.in;
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));

        String userName = "";
        char[] password = new char[0];

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
        return userName + "%-%" + passString;

    }
}