package ru.daniilazarnov.handler.sql;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class QuerySQL {
    public boolean tryAuthInStorage(Connection conn, String login, String pass) {
        //noinspection CheckStyle
        try {
            PreparedStatement statement = conn.prepareStatement("SELECT username FROM clientstore WHERE username = ? AND password = ?");
            statement.setString(1, login);
            statement.setString(2, pass);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
        ConnectionService.close(conn);
        }
        return false;
    }


    @SuppressWarnings("CheckStyle")
    public boolean tryToRegistNewUser(Connection conn, String username,
                                      String password, String userStorage) {
        try (PreparedStatement state = conn.prepareStatement("INSERT INTO clientstore (username, password,userStorage) VALUES (?, ?, ?)")) {
            conn.setAutoCommit(false);
            state.setString(1, username);
            state.setString(2, password);
            state.setString(3, userStorage);
            int row = state.executeUpdate();
            conn.commit();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            ConnectionService.rollback(conn);
            return false;
        } finally {
            ConnectionService.close(conn);
        }
    }
    public boolean isLoginInDb(Connection con, String login) {
        try (PreparedStatement state = con.prepareStatement("SELECT username FROM clientstore WHERE username = ? ")) {
            state.setString(1, login);
          ResultSet rs = state.executeQuery();
            if (rs.next()) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConnectionService.close(con);
        }
        return false;
    }
    public String tryUserStorage(Connection connect, String login) {
        try {
            PreparedStatement state = connect.prepareStatement("SELECT userStorage FROM clientstore WHERE login = ?");
            state.setString(1, login);
            ResultSet rs = state.executeQuery();
            if (rs.next()) {
                return rs.getString("userStorage");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConnectionService.close(connect);
        }
        return null;
    }
}
