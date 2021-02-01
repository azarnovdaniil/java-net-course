package server.chat.auth;

import java.sql.*;

public class DataBaseService {

    private Connection connection;
    private Statement statement;

    public DataBaseService() {
        try {
            connect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void connect() throws ClassNotFoundException, SQLException {
        Class.forName("org.sqlite.JDBC");
        connection = DriverManager.getConnection("jdbc:sqlite:main.db");
        statement = connection.createStatement();
    }

    public void disconnect() {
        try {
            statement.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        try {
            connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public String getNameByLoginPass(String login, String pass) throws SQLException {
        PreparedStatement ps = connection.prepareStatement("SELECT name FROM users WHERE login=? and password=?");
        ps.setString(1, login);
        ps.setString(2, pass);
        ResultSet rs = ps.executeQuery();//statement.executeQuery();
        String res;
        if (rs.next()) {
            res = rs.getString(1);
        } else {
            res = null;
        }
        rs.close();
        return res;
    }

    public boolean changeLogin(String login, String loginNew) {
        try {
            PreparedStatement ps = connection.prepareStatement("UPDATE users SET login=? WHERE login=?");
            ps.setString(1, loginNew);
            ps.setString(2, login);
            ps.executeUpdate();
            if (!connection.getAutoCommit()) {
                connection.commit();
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}
