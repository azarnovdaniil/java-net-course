package ru.daniilazarnov;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;

public class DataBaseProcessor {
    private final ConnectionService connector;
    private static final Logger LOGGER = LogManager.getLogger(DataBaseProcessor.class);

    /**
     * Keeps prepared statements to work with data base. Executes them and returns result to work with.
     */

    DataBaseProcessor() {
        connector = new ConnectionService();
    }

    protected String createUser(String login, String password, String authority) {
        Connection connection = connector.connectUserBase();
        String result = "failed";
        if (connection==null) return result;
        try {
            connection.setAutoCommit(false);
            PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO users (login, pass, authority) VALUES (?, ?, ?)"
            );
            statement.setString(1, login);
            statement.setString(2, password);
            statement.setString(3, authority);

            if (statement.executeUpdate() == 1) {
                result = "done";
            }
            connection.commit();
            return result;

        } catch (SQLIntegrityConstraintViolationException e) {
            result = "parameters occupied";
            LOGGER.error("Parameters occupied", LOGGER.throwing(e));

        } catch (SQLException throwables) {
            connector.rollBack(connection);
            LOGGER.error("SWW with creating new user. Rollback committed.", LOGGER.throwing(throwables));
        } finally {
            connector.close(connection);
        }
        return result;
    }

    protected String userCheck(String login, String password) {
        Connection connection = connector.connectUserBase();
        String authority = "";
        if (connection==null) return authority;
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM users WHERE login = ? AND pass = ?"
            );
            statement.setString(1, login);
            statement.setString(2, password);

            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                authority = rs.getString("authority");
            }

        } catch (SQLException throwables) {
            LOGGER.error("SWW with checking user in database.", LOGGER.throwing(throwables));
        } finally {
            connector.close(connection);
        }
        return authority;
    }


}