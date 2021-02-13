package ru.daniilazarnov.auth;

import javafx.scene.web.HTMLEditorSkin;
import ru.daniilazarnov.CommandsType.AuthCommandData;

import java.util.Objects;

public class User {

    private final String login;
    private final String password;


    public User(String login, String password) {
        this.login = login;
        this.password = password;

    }

    public String getLogin() {

        return login;
    }

    public String getPassword() {

        return password;
    }




//    @Override
//    public int hashCode() {
//        return Objects.hash(login, password);
//    }
}