package ru.daniilazarnov;

import java.sql.*;

public class SQLHandler {
    private static Connection connection;
    private static PreparedStatement psGetNickname;
    private static PreparedStatement psRegistration;
    private static PreparedStatement psChangeNick;
    private static PreparedStatement psChangePass;

    public static boolean connect() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:cloud.db");
            prepareAllStatements();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static void prepareAllStatements() throws SQLException {
        psGetNickname = connection.prepareStatement("SELECT nickname FROM users WHERE login = ? AND password = ?;");
        psRegistration = connection.prepareStatement("INSERT INTO users(login, password, nickname) VALUES (? ,? ,? );");
        psChangeNick = connection.prepareStatement("UPDATE users SET nickname = ? WHERE nickname = ?;");
        psChangePass = connection.prepareStatement("UPDATE users SET password = ? WHERE password = ?;");
    }

    public static String getNicknameByLoginAndPassword(String login, String password) {
        String nick = null;
        try {
            psGetNickname.setString(1, login);
            psGetNickname.setString(2, password);
            ResultSet rs = psGetNickname.executeQuery();
            if (rs.next()) {
                nick = rs.getString(1);
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return nick;
    }

    public static boolean registration(String login, String password, String nickname) {
        try {
            psRegistration.setString(1, login);
            psRegistration.setString(2, password);
            psRegistration.setString(3, nickname);
            psRegistration.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void disconnect() {
        try {
            psRegistration.close();
            psGetNickname.close();
            psChangeNick.close();
            psChangePass.close ();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
