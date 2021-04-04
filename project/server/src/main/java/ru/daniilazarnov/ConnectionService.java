package ru.daniilazarnov;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionService {

    private final String[] dBparam;
    private static final Logger LOGGER = LogManager.getLogger(ConnectionService.class);

    ConnectionService() {
        this.dBparam = ServerConfigReader.getDBparam();
    }

    protected Connection connectUserBase() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            return DriverManager.getConnection(dBparam[0], dBparam[1], dBparam[2]);
        } catch (SQLException | ClassNotFoundException throwables) {
            LOGGER.error("SWW with connection to DataBase", LOGGER.throwing(throwables));
            throw new RuntimeException();
        }

    }

    protected void rollBack(Connection connection) {
        try {
            connection.rollback();
        } catch (SQLException throwables) {
            LOGGER.error("SWW with connection to DataBase (rollback)", LOGGER.throwing(throwables));
        }
    }

    protected void close(Connection connection) {
        try {
            connection.close();
        } catch (SQLException throwables) {
            LOGGER.error("SWW with connection to DataBase (close)", LOGGER.throwing(throwables));
        }
    }
}