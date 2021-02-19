package ru.daniilazarnov.authentication;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class DBConnection {
    private DBConnection() {
    }

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection("jdbc:mysql://localhost:3306/data_vault_2",
                    "root",
                    "root");
        } catch (SQLException e) {
            throw new RuntimeException("SWW during connection");
        }
    }

    public static void close(Connection connection) {
        try {
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException("SWW during closing connection", e);
        }
    }
}
