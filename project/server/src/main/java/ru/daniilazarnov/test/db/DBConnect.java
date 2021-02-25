package ru.daniilazarnov.test.db;


import ru.daniilazarnov.test.entity.User;

import java.sql.*;
import java.util.Set;

public class DBConnect {
    private User user;

    public User doAuth(String login, String password) {
        if (getUser(login, password)){
            return user;
        }
        else {
            System.out.println("wrong login or password");
            return null;
        }
    }

    public boolean doReg(String login, String password){
        return addUser(login, password);
    }



    private boolean getUser(String login, String password){
        Connection connection = getConnection();
        try {
            PreparedStatement statement = connection.prepareStatement("select * from users where login = ? and password = ?");
            statement.setString(1, login);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()){
                user = new User(
                        resultSet.getInt("id"),
                        resultSet.getString("login"),
                        resultSet.getString("password")
                );
                return true;
            }
            return false;
        } catch (SQLException e) {
            throw new RuntimeException("SWW during getUserByEmailAndPassword", e);
        } finally {
            this.close(connection);
        }
    }

    private boolean addUser(String login, String password) {
        Connection connection = getConnection();
        try {
            connection.setAutoCommit(false);
            PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO users (login, password) SELECT ?, ? FROM DUAL WHERE NOT EXISTS (SELECT * FROM users WHERE login = ?);"
            );
            statement.setString(1, login);
            statement.setString(2, password);
            statement.setString(3, login);
            int val = statement.executeUpdate();
            connection.commit();

            return val == 1;

        } catch (SQLException e) {
            this.rollback(connection);
            throw new RuntimeException("SWW during saveInDB", e);
        } finally {
            this.close(connection);
        }
    }

    //==================================================================================================================

    private static Connection getConnection(){
        try {
            return DriverManager.getConnection("jdbc:mysql://localhost/file_server?useSSL=false", "root", "pass");
        } catch (SQLException e) {
            throw new RuntimeException("SWW during DB connection", e);
        }
    }
    private void close(Connection connection){
        try {
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException("SWW during close save operation", e);
        }
    }

    private void rollback(Connection connection){
        try {
            connection.rollback();
        } catch (SQLException e) {
            throw new RuntimeException("SWW during rollback", e);
        }
    }
}

