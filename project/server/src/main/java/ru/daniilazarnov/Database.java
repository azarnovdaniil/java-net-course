package ru.daniilazarnov;


import java.sql.*;

public class Database {
    private static Connection connection;
    private static Statement stmt;

    public static void connect() throws SQLException {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:database.db");
            stmt = connection.createStatement();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static String getNameByLogAndPass(String login, String password) throws SQLException {
        String sql = String.format("SELECT name FROM main where " +
                "login = '%s' and password = '%s'", login, password);
        ResultSet rs = stmt.executeQuery(sql);

        if (rs.next()) {
            return rs.getString(1);
        }
        return null;
    }

    public static boolean isLogin(String name) throws SQLException {
        String sql = String.format("SELECT id FROM main where name = '%s'", name);
        ResultSet rs = stmt.executeQuery(sql);
        return rs.next();
    }

    public static void disconnect() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}