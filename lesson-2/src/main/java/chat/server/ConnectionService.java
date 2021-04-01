package chat.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class ConnectionService {

    public static final String JDBC_MYSQL_S_D_S = "jdbc:mysql://%s:%d/%s";
    public static final String HOST = "127.0.0.1";
    public static final int PORT = 3306;
    public static final String DATABASE = "chat";

    public static Connection connect() {
        try {
            return DriverManager.getConnection(String.format(JDBC_MYSQL_S_D_S, HOST, PORT, DATABASE), "root", "root");
        } catch (SQLException throwables) {
            throw new RuntimeException("SWW", throwables);
        }
    }

    public static void rollback(Connection connection) {
        try {
            connection.rollback();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static void close(Connection connection) {
        try {
            connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
