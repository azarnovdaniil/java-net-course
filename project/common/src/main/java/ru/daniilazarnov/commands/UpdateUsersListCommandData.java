package ru.daniilazarnov.commands;

import java.io.Serializable;
import java.util.List;

public class UpdateUsersListCommandData implements Serializable {

    private final List<String> users;

    public UpdateUsersListCommandData(List<String> users) {
        this.users = users;
    }

    public List<String> getUsers() {
        return users;
    }
}
