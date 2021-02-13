package ru.daniilazarnov.sql_client;

import org.apache.log4j.Logger;

import java.sql.*;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class SQLClient {
    private static final Logger LOG = Logger.getLogger(SQLClient.class);
    private static Connection connection;


    /**
     * Метод проверяет наличие в базе пользователя с заданным логином и паролем
     * возвращает:
     * 1 - если удалось найти пользователя с такой комбинацией логина и пароля и пользователь имеет доступ к
     * локальному хранилищу;
     * 0 - удалось найти пользователя с такой комбинацией логина и пароля и пользователь НЕИМЕЕТ доступ к
     * локальному хранилищу;
     * -1 - не удалось найти пользователя;
     *
     * @param login    логин пользователя
     * @param password пароль пользователя
     * @return целочисленной значение.
     */
     public static synchronized int getAuth(String login, String password) {
        connect();
        PreparedStatement preparedStatement = null;
        String sql = "select status FROM users where login = ? and password = ?";
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, login);
            preparedStatement.setString(2, password);

        } catch (SQLException e) {
            LOG.error("line #62", e);
        }
        try (ResultSet set = preparedStatement.executeQuery()) {  //Если вопрос вернет множество результатов

            if (set.next()) {
                return Integer
                        .parseInt(set.getString(
                                1/*номер столбца или название столбца, например "number_order"*/));
            } //забираем строку
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            disconnect();
        }
        return -1; //вернем -1, если запрос вернулся пустым
    }



    private static void connect() { // для sqLite
        //path from repository root
        String sDbUrl = "jdbc:sqlite:project/server/src/main/java/ru/daniilazarnov/sqlClient/netty_server.db";
        try {
            Class.forName("org.sqlite.JDBC");
            LOG.info("Trying to connect DB");
            connection = DriverManager.getConnection(sDbUrl);

            LOG.info("Connection Established Successfull and the DATABASE NAME IS:"
                    + connection.getMetaData().getDatabaseProductName());
//            statement = connection.createStatement();
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static void disconnect() {
        try {
            connection.close();
            LOG.info("SQL connection close");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}
