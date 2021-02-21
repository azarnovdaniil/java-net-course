package ru.uio.io.auth;

import ru.uio.io.sql.JdbcSQLiteConnection;

import java.sql.SQLException;

public class Registration  {
    private JdbcSQLiteConnection sqLiteConnection;

    public Registration(JdbcSQLiteConnection sqLiteConnection) {
        this.sqLiteConnection = sqLiteConnection;
    }


    public boolean reg(String login, String password) {
        try {
            return sqLiteConnection.insertNewUser(login, password);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
