package ru.atoroschin;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import ru.atoroschin.commands.*;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum CommandsAuth {
    AUTH("auth", (byte) 100, new CommandAuthLoginPass()),
    AUTHOK("authok", (byte) 101, new CommandAuthOk()),
    AUTHERR("autherr", (byte) 102, new CommandAuthErr()),
    AUTHUSER("authdir", (byte) 103, new CommandAuthUser()),
    AUTHUNKNOWN("authunknown", (byte) -1, new CommandAuthUnknown());

    private static final Map<Byte, CommandsAuth> COMMANDS_MAP = Arrays.stream(CommandsAuth.values())
            .collect(Collectors.toMap(commands -> commands.signal, Function.identity()));
    private final String name;
    private final byte signal;
    private final CommandAuth commandApply;

    CommandsAuth(String name, byte signal, CommandAuth commandApply) {
        this.name = name;
        this.signal = signal;
        this.commandApply = commandApply;
    }

    public void sendToServer(ChannelHandlerContext ctx, String readLine) {
        commandApply.send(ctx, readLine, signal);
    }

    public void receiveAndSend(ChannelHandlerContext ctx, ByteBuf buf, AuthService authService, FileWorker fileWorker) {
        commandApply.response(ctx, buf, authService, fileWorker, signal);
    }

    public void receive(ChannelHandlerContext ctx, ByteBuf buf, AuthService authService) {
        commandApply.receive(ctx, buf, authService);
    }

    public static CommandsAuth getCommand(byte code) {
        return COMMANDS_MAP.getOrDefault(code, AUTHUNKNOWN);
    }

    public byte getSignal() {
        return signal;
    }
}
