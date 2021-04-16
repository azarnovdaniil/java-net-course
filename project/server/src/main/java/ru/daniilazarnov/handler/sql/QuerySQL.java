package ru.daniilazarnov.handler.sql;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class QuerySQL {
    public boolean tryAuthInStorage(Connection conn, String login, String pass) {
        try {
            PreparedStatement state = conn.prepareStatement("SELECT name FROM clientside WHERE name = ? AND pass = ?");
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
        try (PreparedStatement state = conn.prepareStatement("INSERT INTO clientside (name, pass) VALUES (?, ?)")) {
            conn.setAutoCommit(false);
            state.setString(1, username);
            state.setString(2, password);
            state.executeUpdate();
            conn.commit();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            ConnectionService.rollback(conn);
        } finally {
            ConnectionService.close(conn);
        }
        return false;
    }
    public boolean deleteFromDb(Connection conn, String username) {
        try (PreparedStatement state = conn.prepareStatement("DELETE FROM clientside WHERE name = ?")) {
            conn.setAutoCommit(false);
            state.setString(1, username);
            state.executeUpdate();
            conn.commit();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            ConnectionService.rollback(conn);
        } finally {
            ConnectionService.close(conn);
        }
        return false;
    }

    public boolean isLoginInDb(Connection con, String login) {
        try (PreparedStatement state = con.prepareStatement("SELECT name FROM clientside WHERE name = ? ")) {
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
}
