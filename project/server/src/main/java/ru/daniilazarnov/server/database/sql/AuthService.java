package ru.daniilazarnov.server.database.sql;

import ru.daniilazarnov.server.database.Authentication;
import ru.daniilazarnov.server.database.DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthService implements Authentication {

    private static final String loginQuery =
            "Select * From public.\"Users\" where \"login\" = ? and \"password\" = ? Limit 1";
    private final ConnectionService connectionService;

    public AuthService(ConnectionService connectionService) {
        this.connectionService = connectionService;
    }

    @Override
    public boolean login(String name, String password) throws DatabaseException {
        Connection connection = null;
        try {

            connection = connectionService.getConnection();
            PreparedStatement statement = connection.prepareStatement(
                    loginQuery);
            statement.setString(1, name);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) return true;
            return false;

        } catch (SQLException e) {
            throw new DatabaseException("Exception has occurred during login operation via database", e);
        } finally {
            if (connection != null) {
                connectionService.close(connection);
            }
        }
    }

}

