package ru.sviridovaleksey.clientHandler;

import ru.sviridovaleksey.Command;
import ru.sviridovaleksey.commands.AuthCommandData;

import java.sql.*;

public class BaseAuthService {

    private static Connection connection;
    private static PreparedStatement psGetUser;



    public BaseAuthService(){
        try {
            connect();
            prepareStatements();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }


    }


    public String getUsernameByLoginAndPassword(Command command) {

        AuthCommandData data = (AuthCommandData) command.getData();
        String login = data.getLogin();
        String password = data.getPassword();

        try {
            psGetUser.setString(1,login);
            ResultSet resultSet = psGetUser.executeQuery();
            while (resultSet.next()) {
                String userNameFromBAse = resultSet.getString(2);
                String passwordFromBase = resultSet.getString(3);
                String loginFromBase = resultSet.getString(4);
                if (login.equals(loginFromBase) && password.equals(passwordFromBase)) {
                    resultSet.close(); return userNameFromBAse;
                }
            }
            resultSet.close();
            return null;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }

    }


    public static void connect() throws ClassNotFoundException, SQLException {
        Class.forName("org.sqlite.JDBC");
        connection = DriverManager.getConnection("jdbc:sqlite:project/main.db"); //путь и подключение к базе
        Statement stnt = connection.createStatement(); // защищенный запрос
    }


    private static void prepareStatements() throws SQLException {
        psGetUser = connection.prepareStatement("SELECT id,nickName,password,login FROM users WHERE login=?;");
    }

}

