package ru.daniilazarnov;

import io.netty.channel.socket.SocketChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.BiConsumer;

public class UserProfile implements PathHolder {

    private String login;
    private final SocketChannel curChannel;
    private String authority;
    private final ContextData contextData;
    private final BiConsumer<String, SocketChannel> messageToUser;
    private long fileLength;
    private final ServerCommandReader commandReader;
    private static final Logger LOGGER = LogManager.getLogger(UserProfile.class);

    /**
     * PathHolder on Server side. Provides channel handlers with parameters and respond tools for current user.
     * @param login - current user login. Has default value if user is not authorised.
     * @param channel - current channel, this user is using.
     * @param messageToUser - tool to send server respond for user commands.
     */

    UserProfile(String login, SocketChannel channel, BiConsumer<String, SocketChannel> messageToUser) {
        this.login = login;
        this.curChannel = channel;
        this.contextData = new ContextData();
        this.messageToUser = messageToUser;
        this.commandReader = new ServerCommandReader(this);
    }

    @Override
    public void transComplete() {
        sendMessage("true%%%File uploaded successfully!");
        sendMessage("SYSTEM");
        LOGGER.info("File transfer complete.");
    }

    public void executeMessage(ContextData message) {
        this.commandReader.readMessage(message);
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getAuthority() {
        return authority;
    }

    public SocketChannel getCurChannel() {
        return curChannel;
    }

    public String getLogin() {
        return login;
    }

    public ContextData getContextData() {
        return contextData;
    }

    public void sendMessage(String message) {
        this.messageToUser.accept(message, this.curChannel);
    }

    @Override
    public void setFileLength(long length) {
        this.fileLength = length;
    }

    @Override
    public long getFileLength() {
        return this.fileLength;
    }
}
