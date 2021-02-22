package ru.daniilazarnov.server.auth;

import ru.daniilazarnov.server.database.Authentication;
import ru.daniilazarnov.server.database.DatabaseException;

import java.util.HashMap;

public class AuthService {

    private HashMap<String, String> authChannels = new HashMap<>();

    private final Authentication dbAuthentication;

    public AuthService(Authentication dbAuthentication) {
        this.dbAuthentication = dbAuthentication;
    }

    public boolean login(String user, String password, String sessionId) throws AuthenticationException {
        try {
            if (dbAuthentication.login(user, password)) {
                authChannels.put(sessionId, user);
                return true;
            }
            return false;
        } catch (DatabaseException e) {
            throw new AuthenticationException("Exception has occurred during login on the server", e);
        }
    }

    public boolean checkSession(String sessionId) {
        return authChannels.containsKey(sessionId);
    }

    public String getUserBySessionId(String sessionId) {
        if (checkSession(sessionId)) return authChannels.get(sessionId);
        return null;
    }

}
