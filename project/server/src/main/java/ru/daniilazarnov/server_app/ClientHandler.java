package ru.daniilazarnov.server_app;


import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.SocketChannel;
import ru.daniilazarnov.entity.User;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ClientHandler {

    private Map<Channel, User> clients = new HashMap<>();

    public ClientHandler() {
    }

    public void subscribe(Channel ch, User user) {
        clients.put(ch,user);
    }

    public void unsubscribe(Channel ch) {
        clients.remove(ch);
    }

    public String getName(Channel ch){
        return clients.get(ch).getNickname();
    }

    public User getUser(Channel ch){
        return clients.get(ch);
    }

    public boolean isLoggedIn(String user) {
        if(clients.containsValue(user) || !clients.isEmpty()) {
            return true;
        }
        return false;
    }
}
