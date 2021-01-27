package database;

import entity.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DBService implements DatabaseCommands<User> {
    private boolean isChangeSuccessful;

    @Override
    public int initializeDataBase(ArrayList<User> users) {
        Connection connection = DBConnection.getConnection();

        try {
            PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO chat_users (nickname, email, password) VALUES (?, ?, ?)"
            );

            statement.setString(1, users.get(0).getNickname());
            statement.setString(2, users.get(0).getEmail());
            statement.setString(3, users.get(0).getPassword());

            return statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("SWW during initialization", e);
        } finally {
            DBConnection.close(connection);
        }
    }

    @Override
    public List<User> findAll() {
        Connection connection = DBConnection.getConnection();

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM chat_users");

            List<User> users = new ArrayList<>();
            while (resultSet.next()) {
                users.add(new User(
                                resultSet.getInt("id"),
                                resultSet.getString("nickname"),
                                resultSet.getString("email"),
                                resultSet.getString("password")
                        )
                );
            }
            return users;

        } catch (SQLException e) {
            throw new RuntimeException("SWW during find all operation", e);
        } finally {
            DBConnection.close(connection);
        }
    }

    @Override
    public User findUser(String authEmail) {
        Connection connection = DBConnection.getConnection();

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM chat_users " +
                    "WHERE email = '" + authEmail +"';");

            if (resultSet != null) {
                User user = new User();
                while (resultSet.next()) {
                    user.setId(resultSet.getInt("id"));
                    user.setNickname(resultSet.getString("nickname"));
                    user.setEmail(resultSet.getString("email"));
                    user.setPassword(resultSet.getString("password"));
                }
                return user;
            } else {
                System.out.println("There is no users with that email");
                return null;
            }


        } catch (SQLException e) {
            throw new RuntimeException("SWW during find user operation", e);
        } finally {
            DBConnection.close(connection);
        }

    }

    @Override
    public int changeNickname(String oldNickName, String newNickName, String password) {
        Connection connection = DBConnection.getConnection();

        try {
            boolean passCheck = false;
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM chat_users " +
                    "WHERE nickname = '" + oldNickName +"';");
            while (resultSet.next()) {
                passCheck = password.equals(resultSet.getString("password"));
            }

            if (passCheck) {
                PreparedStatement changeNicknameStatement = connection.prepareStatement(
                        "UPDATE chat_users " +
                                "SET nickname = ?" +
                                "WHERE nickname = ?;"
                );
                changeNicknameStatement.setString(1, newNickName);
                changeNicknameStatement.setString(2, oldNickName);

                setChangeSuccessful(true);
                return changeNicknameStatement.executeUpdate();
            }
            setChangeSuccessful(false);
            return 0;

        } catch (SQLException e) {
            throw new RuntimeException("SWW during change nickname operation", e);
        } finally {
            DBConnection.close(connection);
        }

    }

    public boolean isChangeSuccessful() {
        return isChangeSuccessful;
    }
    public void setChangeSuccessful(boolean changeSuccessful) {
        isChangeSuccessful = changeSuccessful;
    }
}
