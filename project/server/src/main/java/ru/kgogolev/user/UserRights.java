package ru.kgogolev.user;

import ru.kgogolev.AccessRights;

import java.util.Map;

public class UserRights {
    public static final Map<String, AccessRights> USERS_RIGHTS = Map.of(
            "nickname1", AccessRights.FULL,
            "nickname2", AccessRights.FULL,
            "nickname3", AccessRights.FULL,
            "nickname4", AccessRights.FULL
    );

}
