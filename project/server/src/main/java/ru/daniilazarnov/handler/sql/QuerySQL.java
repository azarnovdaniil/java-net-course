package ru.daniilazarnov.handler.sql;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class QuerySQL {
    public boolean tryAuthInStorage(Connection conn, String login, String pass) {

        try {
            PreparedStatement state = conn.prepareStatement("SELECT name FROM clientstore WHERE name = ? AND pass = ?");
            state.setString(1, login);
            state.setString(2, pass);
            ResultSet rs = state.executeQuery();
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

    public boolean tryToRegistNewUser(Connection conn, String username, String password) {
        try (PreparedStatement state = conn.prepareStatement("INSERT INTO clientstore (name, pass) VALUES (?, ?)")) {
            conn.setAutoCommit(false);
            state.setString(1, username);
            state.setString(2, password);
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
