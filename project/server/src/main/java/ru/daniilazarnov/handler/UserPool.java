package ru.daniilazarnov.handler;

import io.netty.channel.Channel;

import java.util.LinkedList;
import java.util.List;

public class UserPool {
    private static List<User> pool;

    public static void add(User user) {
        if (pool == null) {
            pool = new LinkedList<>();
            pool.add(user);
        }
        pool.add(user);
    }

    public static void remove(Channel channel) {
        for (User user : pool) {
            if (user.getChannel() == channel) {
                pool.remove(user);
            }
        }
    }

    public static String getUserName(Channel channel) {
        for (User user : pool) {
            if (user.getChannel() == channel) {
                return user.getName();
            }
        }
        return null;
    }
    public static TypeUser getUserType(Channel channel) {
        for (User user : pool) {
            if (user.getChannel() == channel) {
                return user.getTypeUser();
            }
        }
        return null;
    }
}
