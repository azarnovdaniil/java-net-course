package helpers;

import ru.daniilazarnov.CredentialsEntry;

import java.sql.SQLException;
import java.util.HashSet;
import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Set;

public class DataBaseHelper {

    private static final String SELECT_ALL_USERS = "select login, password, nickname from chat.users";
    private static final String UPDATE_USER_NICKNAME = "update chat.users set nickname = ? where nickname = ?";
    private static final int PORT = 3306;
    private static final String DATABASE = "chat";
    private static final String HOST = "127.0.0.1";
    private static final String DRIVER_STRING = "%s://%s:%d/%s";

    //todo use config
    public static Connection connect() {
        try {
            return DriverManager.getConnection(String.format(DRIVER_STRING, HOST, PORT, DATABASE), "root", "root");
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

    //пробую кучу autocloseable через try
    public static Set<CredentialsEntry> getUsers() {
        Set<CredentialsEntry> result = new HashSet<>();

        try (Connection con = connect()) {
            try (Statement s = con.createStatement()) {
                s.execute(SELECT_ALL_USERS);

                try (ResultSet rs = s.getResultSet()) {
                    while (rs.next()) {
                        result.add(new CredentialsEntry(
                                rs.getString("login"),
                                rs.getString("password"),
                                rs.getString("nickname")
                        ));
                    }
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return result;
    }

    public static boolean updateNickname(String oldNick, String newNick) {
        boolean result = false;
        Connection con = connect();

        try {
            con.setAutoCommit(false);
            try (PreparedStatement ps = con.prepareStatement(UPDATE_USER_NICKNAME)) {
                ps.setString(1, newNick);
                ps.setString(2, oldNick);

                if (ps.executeUpdate() > 0) {
                    con.commit();
                    result = true;
                }
            }
        } catch (SQLException throwables) {
            rollback(con);
            throwables.printStackTrace();
        } finally {
            close(con);
        }

        return result;
    }
}
