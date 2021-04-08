package ru.daniilazarnov.handler;

import ru.daniilazarnov.Server;
import ru.daniilazarnov.domain.MyMessage;
import ru.daniilazarnov.handler.sql.ConnectionService;
import ru.daniilazarnov.handler.sql.QuerySQL;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CommandServer {
    public CommandServer () {
    }

    protected String authInStorage (String user, String password){
        QuerySQL sql =new QuerySQL();
        if (sql.tryAuthInStorage(ConnectionService.connectMySQL(), user, password) == true){
            return  new String("Wellcome " + user);
        }
        return new String("Wrong username/password");
    }
    protected String regInStorage (String user, String password){
        QuerySQL sql = new QuerySQL();
        if (sql.isLoginInDb(ConnectionService.connectMySQL(), user) == false){

            String addressStorage = Server.SERVER_REPO + "\\" + user;

            sql.tryToRegistNewUser(ConnectionService.connectMySQL(), user, password,addressStorage);
            try {
                Files.createDirectories(Path.of(addressStorage));
            } catch (IOException e) {
                e.printStackTrace();
            }
            return  new String("Registration in the system is completed");
        }else
            return  new String("User "+user+ " is alreade exist");
}
    protected StringBuilder callHelpManual () {
        File f = new File("project/server/src/help.txt");
        BufferedReader fin = null;
        try {
            fin = new BufferedReader(new FileReader(f));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = fin.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
            }
            return sb;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
