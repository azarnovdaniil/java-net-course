package ru.uio.io.auth;

import ru.uio.io.entity.User;

import java.util.Optional;

public interface AuthenticationService {
    Optional<User> doAuth(String login, String password);
}
