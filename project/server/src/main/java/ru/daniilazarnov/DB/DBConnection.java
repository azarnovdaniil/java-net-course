package ru.daniilazarnov.DB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class DBConnection {
    private DBConnection() {
    }

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection("jdbc:mysql://localhost:3306/storage_users_db",
                    "root",
                    "aduqedyda");
        } catch (SQLException e) {
            throw new RuntimeException("Сбой при попытке соединения");
        }
    }

    public static void close(Connection connection) {
        try {
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException("Возникла проблема во время закрытия соединения", e);
        }
    }
}
