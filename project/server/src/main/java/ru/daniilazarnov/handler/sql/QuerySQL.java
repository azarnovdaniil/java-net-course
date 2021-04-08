package ru.daniilazarnov.handler.sql;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class QuerySQL {
    public boolean tryAuthInStorage(Connection connection, String login, String pass){
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT username FROM clientstore WHERE username = ? AND password = ?");
            statement.setString(1, login);
            statement.setString(2, pass);
            ResultSet rs = statement.executeQuery();
            if (rs.next()){
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
        ConnectionService.close(connection);
        }
        return false;
    }
    public boolean tryToRegistNewUser (Connection connection, String username, String password, String userStorage) {
        try(PreparedStatement statement = connection.prepareStatement("INSERT INTO clientstore (username, password,userStorage) VALUES (?, ?, ?)")) {
            connection.setAutoCommit(false);
            statement.setString(1, username);
            statement.setString(2, password);
            statement.setString(3, userStorage);
            int row = statement.executeUpdate();
            connection.commit();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            ConnectionService.rollback(connection);
            return false;
        }finally {
            ConnectionService.close(connection);
        }
    }
    public boolean isLoginInDb (Connection connection, String login) {
        try (PreparedStatement statement = connection.prepareStatement("SELECT username FROM clientstore WHERE username = ? "))
        {
            statement.setString(1, login);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            ConnectionService.close(connection);
        }
        return false;
    }
    public String tryUserStorage(Connection connection, String login){
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT userStorage FROM clientstore WHERE login = ?");
            statement.setString(1, login);
            ResultSet rs = statement.executeQuery();
            if (rs.next()){
                return rs.getString("userStorage");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            ConnectionService.close(connection);
        }
        return null;
    }
}
