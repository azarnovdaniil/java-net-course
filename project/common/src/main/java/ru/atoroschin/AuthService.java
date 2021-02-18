package ru.atoroschin;

public interface AuthService {
    int getUserID(String login, String pass);

    String getUserFolder(int id);

    int getMaxVolume(int id);
}
