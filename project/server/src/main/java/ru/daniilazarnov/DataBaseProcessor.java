package ru.daniilazarnov;

import java.sql.*;

public class DataBaseProcessor {
    private ConnectionService connector;

    DataBaseProcessor(){
        connector=new ConnectionService();
    }

    public String createUser (String login, String password, String authority){
        Connection connection = connector.connectUserBase();
        String result="failed";
        try {
            connection.setAutoCommit(false);
            PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO users (login, pass, authority) VALUES (?, ?, ?)"
            );
            statement.setString(1, login);
            statement.setString(2,password);
            statement.setString(3,authority);

            if (statement.executeUpdate()==1){
                result="done";
            }
            connection.commit();
            return result;

        } catch (SQLIntegrityConstraintViolationException e){
            result="parameters occupied";
        }catch (SQLException throwables) {
            connector.rollBack(connection);
            throw new RuntimeException("SWW with creating new user. Rollback committed.");
        }finally {
            connector.close(connection);
        }
        return result;
    }

    public String userCheck (String login, String password){
        Connection connection = connector.connectUserBase();
        String authority="";
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM users WHERE login = ? AND pass = ?"
            );
            statement.setString(1, login);
            statement.setString(2, password);

            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                authority = rs.getString("authority");
            }

        } catch (SQLException throwables) {
            throw new RuntimeException("SWW with checking user in database.");
        }finally {
            connector.close(connection);
        }
        return authority;
    }


}