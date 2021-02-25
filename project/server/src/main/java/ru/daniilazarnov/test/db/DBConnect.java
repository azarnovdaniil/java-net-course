package ru.daniilazarnov.test.db;


import ru.daniilazarnov.test.entity.User;

import java.sql.*;
import java.util.Set;

public class DBConnect {
    private User user;

    public boolean doAuth(String login, String password, Set<User> users, User user) {
        if (getUser(login, password) != null){
            user = getUser(login, password);
            if (users.stream().noneMatch(u -> u.getLogin().equals(login))){
                users.add(user);
                System.out.println(user.getLogin() + " is logged in");
                return true;
            } else {
                System.out.println("This user already logged in");
                return false;
            }
        } else {
            System.out.println("wrong login or password");
            return false;
        }
    }



    private User getUser(String login, String password){
        Connection connection = getConnection();
        try {
            PreparedStatement statement = connection.prepareStatement("select * from users where login = ? and password = ?");
            statement.setString(1, login);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()){
                return new User(
                        resultSet.getInt("id"),
                        resultSet.getString("login"),
                        resultSet.getString("password")
                );
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException("SWW during getUserByEmailAndPassword", e);
        } finally {
            DBConnect.close(connection);
        }
    }

    private static Connection getConnection(){
        try {
            return DriverManager.getConnection("jdbc:mysql://localhost/file_server?useSSL=false", "root", "pass");
        } catch (SQLException e) {
            throw new RuntimeException("SWW during DB connection", e);
        }
    }
    private static void close(Connection connection){
        try {
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException("SWW during close save operation", e);
        }
    }

    public void rollback(Connection connection){
        try {
            connection.rollback();
        } catch (SQLException e) {
            throw new RuntimeException("SWW during rollback", e);
        }
    }
}

