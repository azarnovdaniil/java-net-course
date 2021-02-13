package ru.uio.io.entity;

public class User {
    private String nickname;
    private String email;
    private String password;

    public User(String nickname, String email, String password) {
        this.nickname = nickname;
        this.email = email;
        this.password = password;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickname() {
        return nickname;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return "User{"
                + "nickname='" + nickname + '\''
                + ", email='" + email + '\''
                + ", password='" + password + '\''
                + '}';
    }
}
