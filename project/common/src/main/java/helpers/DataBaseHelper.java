package helpers;

import messages.AuthMessage;

import java.io.IOException;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.HashSet;
import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Set;

public class DataBaseHelper {

    private static final String SELECT_ALL_USERS = "select login, password, nickname from chat.users";
    private static final String UPDATE_USER_NICKNAME = "update chat.users set nickname = ? where nickname = ?";
    private static final String DRIVER_STRING = "%s://%s:%d/%s";
    ConfigHelper config;

    public DataBaseHelper(Path cofigPath) {
        try {
            config = new ConfigHelper(cofigPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            config.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Connection connect() {
        try {
            return DriverManager.getConnection(String.format(
                    DRIVER_STRING,
                    config.getProperty("app.db.driver"),
                    config.getProperty("app.db.host"),
                    Integer.parseInt(config.getProperty("app.db.port")),
                    config.getProperty("app.db.name")),
                    config.getProperty("app.db.user"),
                    config.getProperty("app.db.pass"));
        } catch (SQLException throwables) {
            throw new RuntimeException("SWW", throwables);
        }
    }

    public void rollback(Connection connection) {
        try {
            connection.rollback();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void close(Connection connection) {
        try {
            connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public Set<AuthMessage> getUsers() {
        Set<AuthMessage> result = new HashSet<>();

        try (Connection con = connect()) {
            try (Statement s = con.createStatement()) {
                s.execute(SELECT_ALL_USERS);

                try (ResultSet rs = s.getResultSet()) {
                    while (rs.next()) {
                        result.add(new AuthMessage(
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

    public boolean updateNickname(String oldNick, String newNick) {
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
