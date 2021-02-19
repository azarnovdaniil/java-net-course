package ru.daniilazarnov.server.auth;

public class AuthenticationException extends Exception {

    public AuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }
}
