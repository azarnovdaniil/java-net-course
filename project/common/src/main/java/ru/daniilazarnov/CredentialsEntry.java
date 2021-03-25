package ru.daniilazarnov;

public class CredentialsEntry {
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

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
