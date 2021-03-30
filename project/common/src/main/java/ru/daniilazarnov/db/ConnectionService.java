package ru.daniilazarnov.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class ConnectionService {

    public static Connection connect() {
        try {
            return DriverManager.getConnection("jdbc:mysql://localhost:3306/chat?serverTimezone=Europe/Moscow", "root",
                    "pass");
        } catch (SQLException throwable) {
            throw new RuntimeException("SWW", throwable);
        }
    }

    public static void rollback(Connection connection) {
        try {
            connection.rollback();
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
    }

    public static void close(Connection connection) {
        try {
            connection.close();
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
    }
}
