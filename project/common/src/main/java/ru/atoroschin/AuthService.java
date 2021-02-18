package ru.atoroschin;

public interface AuthService {
    int getUserID(String login, String pass);

    String getUserFolder(int ID);

    int getMaxVolume(int id);
}
