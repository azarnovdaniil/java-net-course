package ru.daniilazarnov;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionService {

    protected Connection connectUserBase(){
        String [] DBparam = Server.getDBparam();
        try {
            Class.forName("com.mysql.jdbc.Driver");
            return DriverManager.getConnection(DBparam[0], DBparam[1], DBparam[2]);
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
            throw new RuntimeException("SWW with connection to DataBase");
        }

    }
    protected void rollBack (Connection connection) {
        try {
            connection.rollback();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new RuntimeException("SWW with connection to DataBase");
        }
    }

    protected void close (Connection connection){
        try {
            connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new RuntimeException("SWW with connection to DataBase");
        }
    }
}