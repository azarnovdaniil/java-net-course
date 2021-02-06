package ru.uio.io.sql;

import ru.uio.io.entity.User;

import java.io.File;
import java.sql.*;

public class JdbcSQLiteConnection {
    private Connection conn;
    private String dbUrl;

    public JdbcSQLiteConnection() throws SQLException {
        this.dbUrl = "jdbc:sqlite:sql" + File.separator + "db" + File.separator + "test.db";
        this.conn = DriverManager.getConnection(this.dbUrl);
        DatabaseMetaData dm = (DatabaseMetaData) conn.getMetaData();
        System.out.println("Driver name: " + dm.getDriverName());
        System.out.println("Driver version: " + dm.getDriverVersion());
        System.out.println("Product name: " + dm.getDatabaseProductName());
        System.out.println("Product version: " + dm.getDatabaseProductVersion());
        String sql = "CREATE TABLE IF NOT EXISTS user (\n"
                + "	id   INTEGER not null\n" +
                "        constraint user_pk\n" +
                "            primary key autoincrement,\n"
                + "	name varchar not null,\n"
                + "	mail varchar NOT NULL,\n"
                + "	pass varchar NOT NULL\n"
                + ");create unique index user_id_uindex\n" +
                "    on user (id);";
        Statement stmt = conn.createStatement();
            // create a new table
        stmt.execute(sql);
    }

    public User checkLoginAndPass(String login, String pass) throws SQLException {
        PreparedStatement preparedStatement = conn.prepareStatement("select name, mail, pass from user where mail=? and pass=?");
        preparedStatement.setString(1, login);
        preparedStatement.setString(2, pass);
        ResultSet resultSet = preparedStatement.executeQuery();
        User user = null;
        if (resultSet.next()) {
            String name = resultSet.getString("name");
            String mail = resultSet.getString("mail");
            String password = resultSet.getString("pass");
            user = new User(name, mail, password);
        }
        return user;
    }

    public void close() throws SQLException {
        conn.close();
    }
}
