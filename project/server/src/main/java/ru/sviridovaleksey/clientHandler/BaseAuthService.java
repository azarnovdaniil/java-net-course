package ru.sviridovaleksey.clientHandler;

import ru.sviridovaleksey.Command;
import ru.sviridovaleksey.commands.AuthCommandData;
import java.sql.*;

public class BaseAuthService {

    private Connection connection;
    private PreparedStatement psGetUser;
    private final String baseAddress;



    public BaseAuthService(String baseAddress) {
        this.baseAddress = baseAddress;
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
        final int columnIndexName = 2;
        final int columnIndexPassword = 3;
        final int columnIndexLogin = 4;
        try {
            psGetUser.setString(1, login);
            ResultSet resultSet = psGetUser.executeQuery();
            while (resultSet.next()) {
                String userNameFromBAse = resultSet.getString(columnIndexName);
                String passwordFromBase = resultSet.getString(columnIndexPassword);
                String loginFromBase = resultSet.getString(columnIndexLogin);
                if (login.equals(loginFromBase) && password.equals(passwordFromBase)) {
                    resultSet.close(); return userNameFromBAse;
                }
            }
            resultSet.close();
            return null;
        } catch (SQLException throwable) {
            throwable.printStackTrace();
            return null;
        }

    }


    public void connect() throws ClassNotFoundException, SQLException {
        Class.forName("org.sqlite.JDBC");
        connection = DriverManager.getConnection("jdbc:sqlite:" + baseAddress + "\\main.db");
        Statement statement = connection.createStatement(); // защищенный запрос
    }


    private void prepareStatements() throws SQLException {
        psGetUser = connection.prepareStatement("SELECT id,nickName,password,login FROM users WHERE login=?;");
    }

}

