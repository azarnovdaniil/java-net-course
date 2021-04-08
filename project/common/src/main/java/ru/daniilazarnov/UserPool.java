package ru.daniilazarnov;

import net.sf.saxon.expr.instruct.ForEach;

import java.nio.channels.Channel;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class UserPool {
    private static List<User> pool;

    private static void add (User user) {
        if (pool == null) {
            pool = new LinkedList<>();
            pool.add(user);
        }
        pool.add(user);
    }

    private static void remove (Channel channel) {
        for (User user : pool
        ) {
            if (user.getChannel() == channel) {
                pool.remove(user);
            }
        }
    }

    private static String getUserName (Channel channel) {
        for (User user : pool)
        {
            if (user.getChannel() == channel) {
                return user.getName();
            }
        }
        return null;
    }
}
