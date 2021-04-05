package ru.daniilazarnov.authService;

public class AuthUser implements IAuthentication {


    @Override
    public boolean auth(String login, String password) {
        return false;
    }
}
