package ru.daniilazarnov.auth;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import org.apache.log4j.Logger;
import ru.daniilazarnov.Command;
import ru.daniilazarnov.ProgressBar;
import ru.daniilazarnov.ReceivingAndSendingStrings;
import ru.daniilazarnov.network.ClientNetworkHandler;

import java.io.*;

import static ru.daniilazarnov.network.NetworkCommunicationMethods.*;
import static ru.daniilazarnov.constants.Constants.*;


public class Auth {
    private static final Logger LOG = Logger.getLogger(Auth.class);


    public void authentication(ByteBuf buf, ChannelHandlerContext ctx) {
        String right = ReceivingAndSendingStrings.receiveAndEncodeString(buf);
        if (right.equals("1")) {
            ClientNetworkHandler.setAuth(true);

        } else {
            ctx.close();
            System.exit(0);
        }
        System.out.println(" [Доступ к удаленной базе " + (right.equals("1") ? "разрешен" : "отсутствует") + "]");

    }


    public String getStatusAuth() {
        return "Регистрация " + (ClientNetworkHandler.isAuth() ? "" : "не") + " подтверждена";
    }

    public boolean auth() {
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
        ProgressBar.start(SEVEN);
        sendStringAndCommandByte((userName + "%-%" + passString), Command.AUTH.getCommandByte());
        return false;
    }
}
