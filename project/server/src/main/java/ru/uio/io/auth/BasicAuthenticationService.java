package ru.uio.io.auth;

import ru.uio.io.entity.User;
import ru.uio.io.sql.JdbcSQLiteConnection;

import java.sql.SQLException;
import java.util.Optional;

public class BasicAuthenticationService implements AuthenticationService {
    private JdbcSQLiteConnection sqLiteConnection;

    public BasicAuthenticationService(JdbcSQLiteConnection sqLiteConnection) {
        this.sqLiteConnection = sqLiteConnection;
    }

    @Override
    public Optional<User> doAuth(String email, String password) {
        System.out.println(email);
        System.out.println(password);
        try {
            User user = sqLiteConnection.checkLoginAndPass(email, password);
            System.out.println(user);
            if(user != null) return Optional.of(user);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public void close() {
        sqLiteConnection.close();
    }
}
