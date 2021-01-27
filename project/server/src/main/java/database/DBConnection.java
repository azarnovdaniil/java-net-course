package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    DBConnection() {}

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection("jdbc:mysql://localhost:3306/chat_users_database",
                    "root",
                    "");
        }
        catch (SQLException e) {
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
