package ru.daniilazarnov.server.auth;

import ru.daniilazarnov.server.database.Authentication;
import ru.daniilazarnov.server.database.DatabaseException;

import java.util.HashMap;

public class AuthService {

    private HashMap<String, String> authChannels;

    private final Authentication dbAuthentication;

    public AuthService(Authentication dbAuthentication) {
        this.dbAuthentication = dbAuthentication;
    }

    public boolean login(String user, String password, String channelId) throws AuthenticationException {
        try {
            if (dbAuthentication.login(user, password)) {
                authChannels.put(channelId, user);
                return true;
            }
            return false;
        } catch (DatabaseException e) {
            throw new AuthenticationException("Exception has occurred during login on the server", e);
        }
    }

    public String checkSession(String channelId) {
        return authChannels.get(channelId);
    }

}
