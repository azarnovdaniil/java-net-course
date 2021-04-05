package ru.daniilazarnov.authService;

import java.sql.*;

public class DataBaseConnection {
    Connection connection;
    Statement statement;
    PreparedStatement preparedStatementAuth;
    private static final String url = "jdbc:mysql://localhost:3306";
    private static final String user = "admin";
    private static final String password = "password";

    public DataBaseConnection() {
        try {
            connection = DriverManager.getConnection(url, user, password);
            statement = connection.createStatement();
            preparedStatementAuth = connection.prepareStatement("SELECT * FROM patients WHERE login = ?");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
