package ru.daniilazarnov.db;

public class AuthenticationService {
    private UserRepository users;

    public AuthenticationService() {
        users = new UserRepository();
    }

    public String findNicknameByLoginAndPassword(String login, String password) {
        CredentialsEntry user = users.findUser(login, password);
        if (user != null) {
            return user.getNickname();
        }
        return null;
    }

    public void updateNickname(String newName, String oldName) {
        users.updateUser(newName, oldName);
    }

    public static class CredentialsEntry {
        private String login;
        private String password;
        private String nickname;

        public CredentialsEntry(String login, String password, String nickname) {
            this.login = login;
            this.password = password;
            this.nickname = nickname;
        }


        public String getLogin() {
            return login;
        }

        public String getPassword() {
            return password;
        }

        public String getNickname() {
            return nickname;
        }
    }
}
