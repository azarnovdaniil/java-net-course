package ru.daniilazarnov.actual;

public enum  Signals {
    TEXT ((byte) 5),
    AUTH ((byte) 10),
    REG ((byte) 15),
    UPLOAD ((byte) 25),
    START_UPLOAD ((byte) 35),
    DOWNLOAD ((byte) 36),
    LS ((byte) 45),
    RM ((byte) 50);

    private final byte signal;

    Signals(byte signal) {
        this.signal = signal;
    }

    public byte get() {
        return signal;
    }
}
