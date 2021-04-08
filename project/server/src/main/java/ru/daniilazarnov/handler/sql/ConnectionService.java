package ru.daniilazarnov.handler.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class ConnectionService {
    private ConnectionService () {
    }

    public static Connection connectSQLite () {
        try {
            return DriverManager.getConnection("jdbc:sqlite:server/database.db");
        } catch (SQLException throwables) {
            throw new RuntimeException("SWW", throwables);
        }
    }
    public static Connection connectPostGreSQL () {
        try {
            return DriverManager.getConnection("jdbc:postgresql:server/database.db");
        } catch (SQLException throwables) {
            throw new RuntimeException("SWW", throwables);
        }
    }
    public static Connection connectMySQL () {
        try {
            return DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/client", "root", "Njgjhbr212");
        } catch (SQLException throwables) {
            throw new RuntimeException("SWW", throwables);
        }
    }

    public static void rollback (Connection connection) {
        try {
            connection.rollback();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void close (Connection connection) {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
