package server.chat;

import java.util.Objects;

public class User {
    private final String name;
    private final String login;
    private final String password;
    private final String nickName;


    public User(String name, String login, String password, String nikName) {
        this.name = name;
        this.login = login;
        this.password = password;
        this.nickName = nikName;
    }

    public String getName() {
        return name;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return name.equals(user.name) &&
                login.equals(user.login) &&
                password.equals(user.password) &&
                nickName.equals(user.nickName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, login, password, nickName);
    }

    public String getNickName() {
        return nickName;
    }
}
