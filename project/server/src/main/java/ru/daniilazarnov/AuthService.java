package ru.daniilazarnov;
public interface AuthService {
    String getNicknameByLoginAndPassword(String login, String password);

    boolean registration(String login, String password, String nickname);
}