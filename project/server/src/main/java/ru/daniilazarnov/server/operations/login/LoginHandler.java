package ru.daniilazarnov.server.operations.login;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import ru.daniilazarnov.common.FilePackageConstants;
import ru.daniilazarnov.common.handlers.Handler;
import ru.daniilazarnov.common.handlers.HandlerException;
import ru.daniilazarnov.server.auth.AuthService;
import ru.daniilazarnov.server.auth.AuthenticationException;

import java.nio.charset.StandardCharsets;

public class LoginHandler implements Handler {

    private final AuthService authService;
    private final Channel channel;
    private ByteBuf buf;

    private LoginHandlerState state = LoginHandlerState.NAME_LENGTH;
    private int nameLength;
    private String name;
    private int passLength;
    private String pass;


    public LoginHandler(AuthService authService, ByteBuf buf, Channel channel) {
        this.authService = authService;
        this.buf = buf;
        this.channel = channel;
    }

    @Override
    public void handle() throws HandlerException {
        if (state == LoginHandlerState.NAME_LENGTH) {
            if (buf.readableBytes() >= FilePackageConstants.NAME_LENGTH_BYTES.getCode()) {
                nameLength = buf.readInt();
                state = LoginHandlerState.NAME;
            }
        }

        if (state == LoginHandlerState.NAME) {
            if (buf.readableBytes() >= nameLength) {
                byte[] nameBytes = new byte[nameLength];
                buf.readBytes(nameBytes);
                name = new String(nameBytes, StandardCharsets.UTF_8);
                state = LoginHandlerState.PASS_LENGTH;
            }
        }

        if (state == LoginHandlerState.PASS_LENGTH) {
            if (buf.readableBytes() >= FilePackageConstants.NAME_LENGTH_BYTES.getCode()) {
                passLength = buf.readInt();
                state = LoginHandlerState.PASS;
            }
        }

        if (state == LoginHandlerState.PASS) {
            if (buf.readableBytes() >= passLength) {
                byte[] passBytes = new byte[passLength];
                buf.readBytes(passBytes);
                pass = new String(passBytes, StandardCharsets.UTF_8);
                try {
                    if (authService.login(name, pass, channel.id().toString())) {
                        System.out.println("Authentication has benn completed");
                    } else {
                        System.out.println("Incorrect login or password. Authentication hasn't benn completed");
                    }
                } catch (AuthenticationException e) {
                    throw new HandlerException("Exception has been occurred via authentication", e);
                }
                state = LoginHandlerState.COMPLETE;
            }
        }
    }

    @Override
    public boolean isComplete() {
        return state == LoginHandlerState.COMPLETE;
    }
}
